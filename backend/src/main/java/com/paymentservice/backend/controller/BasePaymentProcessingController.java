package com.paymentservice.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.paymentservice.backend.service.BankCardProcessingService;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Payment Service API", description = "API для проведения оплаты различными способами")
public class BasePaymentProcessingController {
    private final BankCardProcessingService bankProcessingService;
    private final static String CONTEXT_ATTRIBUTE_NAME = "paymentRequest";
    
    @PostMapping("/bankcard")
    @Operation(summary = "Осуществить платеж по реквизитам банковской карты")
    public BankCardPaymentResponse pay(@RequestBody BankCardPaymentRequest bankCardPaymentRequest) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute(CONTEXT_ATTRIBUTE_NAME, bankCardPaymentRequest, WebRequest.SCOPE_REQUEST);
        }
        return bankProcessingService.processBankPayment(bankCardPaymentRequest);
    }
}
