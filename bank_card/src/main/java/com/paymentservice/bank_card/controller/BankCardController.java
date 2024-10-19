package com.paymentservice.bank_card.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.dto.PaymentResponse;
import com.paymentservice.bank_card.service.CardService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bankcard")
public class BankCardController {
    private final CardService cardService;

    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest) {
        return cardService.makePayment(paymentRequest);
    }
}
