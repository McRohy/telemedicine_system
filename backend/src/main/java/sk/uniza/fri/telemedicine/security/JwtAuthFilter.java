package sk.uniza.fri.telemedicine.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter for JWT authentication which runs with every request.
 * Extends OncePerRequestFilter to ensure it runs once per request.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * Checks every request for a JWT token in the Authorization header.
     * If the token is valid, it reads the email, role and identification number from the token
     * and sets the authentication in Spring Security context else Spring Security
     * will treat the user as unauthenticated and will return 401.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = header.substring(7);
            String email = jwtUtils.getEmailFromToken(token);
            String role = jwtUtils.getRoleFromToken(token);
            String identificationNumber = jwtUtils.getIdentificationNumber(token);

            if (email != null && role != null) {
                 var authToken = new UsernamePasswordAuthenticationToken(
                         email,
                         null,  //password is not needed, it was already verified during login
                         List.of(new SimpleGrantedAuthority(role))
                         );
                 authToken.setDetails(identificationNumber); //extra info about logged user for Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}

