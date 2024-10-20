package com.paymentservice.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentservice.backend.dto.BankCardPaymentRequest;
import com.paymentservice.backend.dto.BankCardPaymentResponse;
import com.paymentservice.backend.service.BankCardProcessingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class BasePaymentProcessingController {
    private final BankCardProcessingService bankProcessingService;
    
    @PostMapping("/bankcard")
    public BankCardPaymentResponse postMethodName(@RequestBody BankCardPaymentRequest bankCardPaymentRequest) {
        return bankProcessingService.processBankPayment(bankCardPaymentRequest);
    }
}
