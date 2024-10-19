package com.paymentservice.bank_card.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.paymentservice.bank_card.domain.Card;
import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.dto.ResponseStatus;
import com.paymentservice.bank_card.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CardRepository cardRepository;
    private final static String SUCCESS = "Success";
    private final static String ERROR = "Error";
    private final static String INVALID_DATA = "Invalid card data";
    private final static String INSUFFICIENT_FUNDS = "Insufficient funds";
    private final static String EXPIRED = "Card expired or incorrect input expire date";
    private final static String SUCCESS_MESSAGE = "Payment ended successfully";

    public Card findByCardNumberWithExceptionHandling(String cardNumber) {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            return new Card();
        }
        return cardRepository.findByCardNumber(cardNumber);
    }

    private boolean checkCardData(Card card, PaymentRequest paymentRequest) {
        return card.getCardNumber() != null && card.getCvv() != null && card.getCvv().equals(paymentRequest.getCvv());
    }

    private boolean hasSufficientFunds(Card card, PaymentRequest paymentRequest) {
        return card.getBalance() != null && card.getBalance().compareTo(paymentRequest.getPaymentSum()) >= 0;
    }

    private boolean isExpiryDateValid(Card card, PaymentRequest paymentRequest) {
        return card.getExpiryDate() != null && card.getExpiryDate().isEqual(paymentRequest.getExpiryDate()) &&
                paymentRequest.getExpiryDate().isAfter(LocalDate.now());
    }

    public ResponseStatus processPayment(PaymentRequest paymentRequest) {
        Card card = findByCardNumberWithExceptionHandling(paymentRequest.getCardNumber());
        if (!checkCardData(card, paymentRequest)) {
            return new ResponseStatus(ERROR, INVALID_DATA);
        } else if (!hasSufficientFunds(card, paymentRequest)) {
            return new ResponseStatus(ERROR, INSUFFICIENT_FUNDS);
        } else if (!isExpiryDateValid(card, paymentRequest)) {
            return new ResponseStatus(ERROR, EXPIRED);
        }
        decreaseBalance(paymentRequest);
        return new ResponseStatus(SUCCESS, SUCCESS_MESSAGE);
    }

    private void decreaseBalance(PaymentRequest paymentRequest) {
        cardRepository.decreaseBalance(paymentRequest.getCardNumber(), paymentRequest.getPaymentSum());
    }
}
