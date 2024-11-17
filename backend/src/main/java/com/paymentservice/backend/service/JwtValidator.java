package com.paymentservice.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtValidator {
    @Value("${secret.key}")
    private String SECRET_KEY;

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date nowDateTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date tokenDateTime = extractClaims(token).getExpiration();
        return tokenDateTime.before(nowDateTime);
    }

    public boolean validateToken(String token, String userId) {
        try {
            String tokenUserId = extractUserId(token);
            return tokenUserId.equals(userId) && !isTokenExpired(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
