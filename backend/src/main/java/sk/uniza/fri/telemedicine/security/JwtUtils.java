package sk.uniza.fri.telemedicine.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JWT tokens.
 * It provides methods for generating, parsing and validating JWT tokens.
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
     * Methods adds a custom claim role and identificationNumber to the token payload.
     */
    public String generateToken(String email, String role, String identificationNumber) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("identificationNumber", identificationNumber)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();     //build together header.payload.signature
    }

    /**
     * Extracts the email (subject) from the token.
     * Checks the token's signature and expiration with the same secret key used for signing.
     */
    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
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

    /**
     * Extracts the identificationNumber claim from the token.
     * Checks the token's signature and expiration with the same secret key used for signing.
     */
    public String getIdentificationNumber(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("identificationNumber", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}