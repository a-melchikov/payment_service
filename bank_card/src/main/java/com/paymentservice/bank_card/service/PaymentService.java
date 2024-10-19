package com.paymentservice.bank_card.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.paymentservice.bank_card.domain.Card;
import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CardRepository cardRepository;

    public void processPayment(PaymentRequest paymentRequest) {
        if (!checkCardData(paymentRequest)) {
            throw new RuntimeException("Error during payment");
        }
        decreaseBalance(paymentRequest);
    }

    private boolean checkCardData(PaymentRequest paymentRequest) {
        Card card = cardRepository.findByCardNumber(paymentRequest.getCardNumber());
        return card.getCvv().equals(paymentRequest.getCvv()) &&
               card.getBalance().compareTo(paymentRequest.getPaymentSum()) >= 0 &&
               card.getExpiryDate().isEqual(paymentRequest.getExpiryDate()) &&
               paymentRequest.getExpiryDate().isAfter(LocalDate.now());
    }

    private void decreaseBalance(PaymentRequest paymentRequest) {
        cardRepository.decreaseBalance(paymentRequest.getCardNumber(), paymentRequest.getPaymentSum());
    }
}
