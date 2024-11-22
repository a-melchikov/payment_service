package com.paymentservice.backend.controller;

import com.paymentservice.backend.dto.FrontendTokenDto;
import com.paymentservice.backend.service.JwtValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/token-validation")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Token Validation API", description = "API для валидации токенов для Frontend")
public class TokenValidationController {
    private final JwtValidator jwtValidator;

    @PostMapping
    @Operation(summary = "Валидировать токен")
    public boolean checkToken(@RequestBody FrontendTokenDto frontendTokenDto) {
        return jwtValidator.validateToken(frontendTokenDto.getToken(), frontendTokenDto.getUserId());
    }
}
