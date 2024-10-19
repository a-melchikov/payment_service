package com.paymentservice.bank_card.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.dto.PaymentResponse;
import com.paymentservice.bank_card.dto.ResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final PaymentService paymentService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentResponse pay(PaymentRequest paymentRequest) {
        ResponseStatus responseStatus = paymentService.processPayment(paymentRequest);
        return new PaymentResponse(paymentRequest.getCardNumber(), responseStatus);
    }
}
