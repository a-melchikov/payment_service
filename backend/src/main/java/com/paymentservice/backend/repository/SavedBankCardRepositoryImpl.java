package com.paymentservice.backend.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.SavedBankCard;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SavedBankCardRepositoryImpl implements SavedBankCardRepository {
    private static final Logger log = LoggerFactory.getLogger(SavedBankCardRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(SavedBankCard savedBankCard) {
        if (savedBankCard.getId() == null) {
            log.info("Persisting new saved bank card for userId: {}", savedBankCard.getUserId());
            try {
                entityManager.persist(savedBankCard);
                log.info("Successfully persisted new saved bank card for userId: {}", savedBankCard.getUserId());
            } catch (Exception e) {
                log.error("Error persisting new saved bank card for userId: {}. Error: {}", savedBankCard.getUserId(), e.getMessage());
            }
        } else {
            log.info("Merging existing saved bank card for userId: {}", savedBankCard.getUserId());
            try {
                entityManager.merge(savedBankCard);
                log.info("Successfully merged saved bank card for userId: {}", savedBankCard.getUserId());
            } catch (Exception e) {
                log.error("Error merging saved bank card for userId: {}. Error: {}", savedBankCard.getUserId(), e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void delete(SavedBankCard savedBankCard) {
        log.info("Deleting saved bank card with ID: {}", savedBankCard.getId());
        try {
            if (entityManager.contains(savedBankCard)) {
                entityManager.remove(savedBankCard);
                log.info("Successfully deleted saved bank card with ID: {}", savedBankCard.getId());
            } else {
                SavedBankCard managed = entityManager.find(SavedBankCard.class, savedBankCard.getId());
                if (managed != null) {
                    entityManager.remove(managed);
                    log.info("Successfully deleted managed saved bank card with ID: {}", savedBankCard.getId());
                } else {
                    log.warn("Saved bank card with ID: {} not found for deletion.", savedBankCard.getId());
                }
            }
        } catch (Exception e) {
            log.error("Error deleting saved bank card with ID: {}. Error: {}", savedBankCard.getId(), e.getMessage());
        }
    }

    @Override
    public List<SavedBankCard> findSavedBankCardsByUserId(Long userId) {
        log.info("Finding saved bank cards for userId: {}", userId);
        try {
            List<SavedBankCard> savedBankCards = entityManager.createQuery(
                            "SELECT s FROM SavedBankCard s WHERE s.userId = :userId", SavedBankCard.class)
                    .setParameter("userId", userId)
                    .getResultList();
            log.info("Found {} saved bank cards for userId: {}", savedBankCards.size(), userId);
            return savedBankCards;
        } catch (Exception e) {
            log.error("Error finding saved bank cards for userId: {}. Error: {}", userId, e.getMessage());
            return null;
        }
    }

    @Override
    public SavedBankCard findByUserIdAndCardNumber(Long userId, String cardNumber) {
        log.info("Finding saved bank card for userId: {} and cardNumber: {}", userId, cardNumber);
        try {
            SavedBankCard savedBankCard = entityManager.createQuery(
                            "SELECT s FROM SavedBankCard s WHERE s.userId = :userId AND s.cardNumber = :cardNumber", SavedBankCard.class)
                    .setParameter("userId", userId)
                    .setParameter("cardNumber", cardNumber)
                    .getSingleResult();
            log.info("Found saved bank card for userId: {} and cardNumber: {}", userId, cardNumber);
            return savedBankCard;
        } catch (Exception e) {
            log.error("Error finding saved bank card for userId: {} and cardNumber: {}. Error: {}", userId, cardNumber, e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isCardExists(Long userId, String cardNumber) {
        log.info("Checking if card exists for userId: {} and cardNumber: {}", userId, cardNumber);
        try {
            Long count = entityManager.createQuery(
                            "SELECT COUNT(s) FROM SavedBankCard s WHERE s.userId = :userId AND s.cardNumber = :cardNumber", Long.class)
                    .setParameter("userId", userId)
                    .setParameter("cardNumber", cardNumber)
                    .getSingleResult();
            boolean exists = count > 0;
            log.info("Card exists for userId: {} and cardNumber: {}: {}", userId, cardNumber, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking if card exists for userId: {} and cardNumber: {}. Error: {}", userId, cardNumber, e.getMessage());
            return false;
        }
    }
}
