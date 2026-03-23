package sk.uniza.fri.telemedicine.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JWT tokens.
 * It  provides methods for generating, parsing and validating JWT tokens.
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private int expirationMs;

    /**
     * Generates a signed JWT token with an expiration time containing the user's email and role.
     * Adds a custom claim role to the payload so the role can be accessed easily without needing to query the database.
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))    //sign with secret key
                .compact();     //build together header.payload.signature
    }

    /**
     * Extracts the email (subject) from the token.
     * Checks the token's signature and expiration with the same secret key used for signing.
     */
    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))  //use same secret key for verification
                    .build()
                    .parseSignedClaims(token) //check signature and expiration
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extracts the role claim from the token.
     * Checks the token's signature and expiration with the same secret key used for signing.
     */
    public String getRoleFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role", String.class);  //get role from payload
        } catch (Exception e) {
            return null;
        }
    }
}