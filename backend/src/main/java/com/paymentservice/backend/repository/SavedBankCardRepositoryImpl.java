package com.paymentservice.backend.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.SavedBankCard;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SavedBankCardRepositoryImpl implements SavedBankCardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(SavedBankCard savedBankCard) {
        if (savedBankCard.getId() == null) {
            entityManager.persist(savedBankCard);
        } else {
            entityManager.merge(savedBankCard);
        }
    }

    @Override
    @Transactional
    public void delete(SavedBankCard savedBankCard) {
        if (entityManager.contains(savedBankCard)) {
            entityManager.remove(savedBankCard);
        } else {
            SavedBankCard managed = entityManager.find(SavedBankCard.class, savedBankCard.getId());
            if (managed != null) {
                entityManager.remove(managed);
            }
        }
    }

    @Override
    public List<SavedBankCard> findSavedBankCardsByUserId(Long userId) {
        return entityManager.createQuery(
                "SELECT s FROM SavedBankCard s WHERE s.userId = :userId", SavedBankCard.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public SavedBankCard findByUserIdAndCardNumber(Long userId, String cardNumber) {
        try {
            return entityManager.createQuery(
                    "SELECT s FROM SavedBankCard s WHERE s.userId = :userId AND s.cardNumber = :cardNumber", SavedBankCard.class)
                    .setParameter("userId", userId)
                    .setParameter("cardNumber", cardNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isCardExists(Long userId, String cardNumber) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(s) FROM SavedBankCard s WHERE s.userId = :userId AND s.cardNumber = :cardNumber", Long.class)
            .setParameter("userId", userId)
            .setParameter("cardNumber", cardNumber)
            .getSingleResult();
        return count > 0;
    }
}
