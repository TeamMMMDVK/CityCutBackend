package com.example.citycutbackend.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtService {
    private static final String SECRET_KEY = "yR2jX4uUOuvktURD3JGcL8tA8e6M6yvf7m5EGARunCZzP6rL5axHTQ9ZgZXRSZqRW0oqr8AvEysOSAGHghRAAA=="; // Brug en miljøvariabel i produktion
    private static final SecretKey SECRET = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
    private static final long EXPIRATION_TIME = 86400000; // 1 dag i millisekunder

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

