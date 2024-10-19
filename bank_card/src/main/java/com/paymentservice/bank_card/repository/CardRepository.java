package com.paymentservice.bank_card.repository;

import java.math.BigDecimal;

import com.paymentservice.bank_card.domain.Card;


public interface CardRepository {
    Card findByCardNumber(String cardNumber);
    void decreaseBalance(String cardNumber, BigDecimal amount);
}
