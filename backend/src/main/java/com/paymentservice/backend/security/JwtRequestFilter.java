package com.paymentservice.backend.security;


import com.paymentservice.backend.service.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final JwtValidator jwtValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/actuator") || requestPath.startsWith("/api/v2")) {
            log.info("Request without authentication, skipping token validation.");
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        String userId = request.getParameter("userId");
        if (token == null) {
            log.info("Missing Authorization token in request to path: {}", requestPath);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid token or userId");
        }
        token = token.replace("Bearer ", "");
        if (userId != null && jwtValidator.validateToken(token, userId)) {
            log.info("Token validated successfully for userId: {}", userId);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            log.info("Invalid token or userId. Request path: {}, userId: {}", requestPath, userId);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid token or userId");
        }
    }
}
