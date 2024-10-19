package com.paymentservice.bank_card.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.paymentservice.bank_card.domain.Card;


public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByCardNumber(String cardNumber);

    @Modifying
    @Query("UPDATE Card c SET c.balance = c.balance - :amount WHERE c.cardNumber = :cardNumber AND c.balance >= :amount")
    int decreaseBalance(String cardNumber, BigDecimal amount);
}
