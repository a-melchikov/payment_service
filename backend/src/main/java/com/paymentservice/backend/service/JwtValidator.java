package com.paymentservice.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtValidator {
    private static final Logger log = LoggerFactory.getLogger(JwtValidator.class);

    @Value("${secret.key}")
    private String SECRET_KEY;

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
        }
        return Jwts.claims();
    }

    private String extractUserId(String token) {
        log.info("Extracting user ID from token...");
        String userId = extractClaims(token).getSubject();
        log.info("Extracted user ID: {}", userId);
        return userId;
    }

    private boolean isTokenExpired(String token) {
        Date nowDateTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date tokenDateTime = extractClaims(token).getExpiration();
        boolean isExpired = tokenDateTime.before(nowDateTime);
        log.info("Token expired: {}", isExpired);
        return isExpired;
    }

    public boolean validateToken(String token, String userId) {
        log.info("Validating token for userId: {}", userId);
        try {
            String tokenUserId = extractUserId(token);
            boolean isValid = tokenUserId.equals(userId) && !isTokenExpired(token);
            if (isValid) {
                log.info("Token is valid for userId: {}", userId);
            } else {
                log.info("Token is invalid for userId: {}", userId);
            }
            return isValid;
        } catch (Exception e) {
            log.error("Token validation failed for userId: {}. Error: {}", userId, e.getMessage());
            return false;
        }
    }
}
