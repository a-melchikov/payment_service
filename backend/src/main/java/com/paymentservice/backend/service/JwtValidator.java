package com.paymentservice.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtValidator {
    private final String SECRET_KEY = "your_secret_key";

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date nowDateTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date tokenDateTime = extractClaims(token).getExpiration();
        System.out.println(nowDateTime);
        System.out.println(tokenDateTime);
        return tokenDateTime.before(nowDateTime);
    }

    public boolean validateToken(String token, String userId) {
        try {
            String tokenUserId = extractUserId(token);
            return tokenUserId.equals(userId) && !isTokenExpired(token);
        } catch (Exception e) {
            System.out.println("Invalid token");
            return false;
        }
    }
}
