package com.paymentservice.bank_card.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.bank_card.dto.BankCardPaymentRequest;
import com.paymentservice.bank_card.dto.BankCardPaymentResponse;
import com.paymentservice.bank_card.dto.ResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final PaymentService paymentService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BankCardPaymentResponse pay(BankCardPaymentRequest paymentRequest) {
        ResponseStatus responseStatus = paymentService.processPayment(paymentRequest);
        return new BankCardPaymentResponse(paymentRequest.getCardNumber(), responseStatus);
    }
}
