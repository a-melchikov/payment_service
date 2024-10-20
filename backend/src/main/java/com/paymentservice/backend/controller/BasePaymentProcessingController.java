package com.paymentservice.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentservice.backend.dto.BankCardPaymentRequest;
import com.paymentservice.backend.dto.BankCardPaymentResponse;
import com.paymentservice.backend.service.BankCardProcessingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Service API", description = "API для проведения оплаты различными способами")
public class BasePaymentProcessingController {
    private final BankCardProcessingService bankProcessingService;
    
    @PostMapping("/bankcard")
    @Operation(summary = "Осуществить платеж по реквизитам банковской карты")
    public BankCardPaymentResponse postMethodName(@RequestBody BankCardPaymentRequest bankCardPaymentRequest) {
        return bankProcessingService.processBankPayment(bankCardPaymentRequest);
    }
}
