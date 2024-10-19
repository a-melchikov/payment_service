package com.paymentservice.bank_card.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.bank_card.domain.Card;
import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.dto.PaymentResponse;
import com.paymentservice.bank_card.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    private boolean checkCardData(PaymentRequest paymentRequest) {
        Card card = cardRepository.findByCardNumber(paymentRequest.getCardNumber());
        if (card.getCvv().equals(paymentRequest.getCvv())
                && card.getBalance().compareTo(paymentRequest.getPaymentSum()) >= 0
                && card.getExpiryDate().isEqual(paymentRequest.getExpiryDate())
                && paymentRequest.getExpiryDate().isBefore(LocalDate.now())) {
            return false;
        }
        return true;
    }

    private void decreaseBalance(PaymentRequest paymentRequest) {
        cardRepository.decreaseBalance(paymentRequest.getCardNumber(), paymentRequest.getPaymentSum());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PaymentResponse makePayment(PaymentRequest paymentRequest) {
        if (!checkCardData(paymentRequest)) {
            throw new RuntimeException("Error during payment");
        }
        decreaseBalance(paymentRequest);
        Card card = cardRepository.findByCardNumber(paymentRequest.getCardNumber());
        return new PaymentResponse(paymentRequest.getCardNumber(), card.getBalance(), "Payment ended successfully");
    }

}
