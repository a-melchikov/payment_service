package com.paymentservice.backend.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.FailedTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class FailedTransactionRepositoryImpl implements FailedTransactionRepository {
    private static final Logger log = LoggerFactory.getLogger(FailedTransactionRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(FailedTransaction failedTransaction) {
        log.info("Saving failed transaction with ID: {}", failedTransaction.getId());
        try {
            entityManager.persist(failedTransaction);
            log.info("Successfully saved failed transaction with ID: {}", failedTransaction.getId());
        } catch (Exception e) {
            log.error("Error saving failed transaction with ID: {}. Error: {}", failedTransaction.getId(), e.getMessage());
        }
    }
}
