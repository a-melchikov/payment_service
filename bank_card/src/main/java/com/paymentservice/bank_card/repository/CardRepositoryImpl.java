package com.paymentservice.bank_card.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.bank_card.domain.Card;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class CardRepositoryImpl implements CardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Card findByCardNumber(String cardNumber) {
        String jpql = "SELECT c FROM Card c WHERE c.cardNumber = :cardNumber";
        TypedQuery<Card> query = entityManager.createQuery(jpql, Card.class);
        query.setParameter("cardNumber", cardNumber);
        return query.getSingleResult();
    }

    @Override
    public boolean existsByCardNumber(String cardNumber) {
        String jpql = "SELECT COUNT(c) FROM Card c WHERE c.cardNumber = :cardNumber";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("cardNumber", cardNumber)
                .getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional
    public void decreaseBalance(String cardNumber, BigDecimal amount) {
        Card card = findByCardNumber(cardNumber);
        card.setBalance(card.getBalance().subtract(amount));
    }
}
