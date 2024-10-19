package com.paymentservice.bank_card.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.dto.PaymentResponse;
import com.paymentservice.bank_card.service.CardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bankcard")
@Tag(name = "Bank Card API", description = "API для управления банковскими картами")
public class BankCardController {
    private final CardService cardService;

    @PostMapping("/pay")
    @Operation(summary = "Оплатить с помощью банковской карты", description = "Процесс платежа по банковской карте")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest) {
        return cardService.pay(paymentRequest);
    }
}
