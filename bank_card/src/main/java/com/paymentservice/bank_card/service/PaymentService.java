package com.paymentservice.bank_card.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.paymentservice.bank_card.domain.Card;
import com.paymentservice.bank_card.repository.CardRepository;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.ResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CardRepository cardRepository;
    private final static String SUCCESS = "Успех";
    private final static String ERROR = "Ошибка";
    private final static String INVALID_DATA = "Неверный номер карты или cvv код";
    private final static String INSUFFICIENT_FUNDS = "Не хватает денег для оплаты";
    private final static String EXPIRED = "Карта просрочена или неверно введена дата";
    private final static String SUCCESS_MESSAGE = "Платеж прошел успешно";

    public Card findByCardNumberWithExceptionHandling(String cardNumber) {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            return new Card();
        }
        return cardRepository.findByCardNumber(cardNumber);
    }

    private boolean checkCardData(Card card, BankCardPaymentRequest paymentRequest) {
        return card.getCardNumber() != null && card.getCvv() != null && card.getCvv().equals(paymentRequest.getCvv());
    }

    private boolean hasSufficientFunds(Card card, BankCardPaymentRequest paymentRequest) {
        return card.getBalance() != null && card.getBalance().compareTo(paymentRequest.getPaymentSum()) >= 0;
    }

    private boolean isExpiryDateValid(Card card, BankCardPaymentRequest paymentRequest) {
        return card.getExpiryDate() != null && card.getExpiryDate().isEqual(paymentRequest.getExpiryDate()) &&
                paymentRequest.getExpiryDate().isAfter(LocalDate.now());
    }

    public ResponseStatus processPayment(BankCardPaymentRequest paymentRequest) {
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

    private void decreaseBalance(BankCardPaymentRequest paymentRequest) {
        cardRepository.decreaseBalance(paymentRequest.getCardNumber(), paymentRequest.getPaymentSum());
    }
}
