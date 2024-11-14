package com.paymentservice.bank_card.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;
import com.paymentservice.dto.ResponseStatus;
import com.paymentservice.bank_card.repository.CardRepository;
import com.paymentservice.bank_card.domain.Card;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final PaymentService paymentService;
    private final CardRepository cardRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BankCardPaymentResponse pay(BankCardPaymentRequest paymentRequest) {
        ResponseStatus responseStatus = paymentService.processPayment(paymentRequest);
        Card card = cardRepository.findByCardNumber(paymentRequest.getCardNumber());
        return new BankCardPaymentResponse(paymentRequest.getUserId(), paymentRequest.getCardNumber(), card.getIssuingBank(), responseStatus);
    }
}
