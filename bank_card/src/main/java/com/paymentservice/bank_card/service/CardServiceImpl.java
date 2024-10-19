package com.paymentservice.bank_card.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.bank_card.domain.Card;
import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.dto.PaymentResponse;
import com.paymentservice.bank_card.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final PaymentService paymentService;
    private final CardRepository cardRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentResponse pay(PaymentRequest paymentRequest) {
        paymentService.processPayment(paymentRequest);
        Card card = cardRepository.findByCardNumber(paymentRequest.getCardNumber());
        return new PaymentResponse(paymentRequest.getCardNumber(), card.getBalance(), "Payment ended successfully");
    }
}
