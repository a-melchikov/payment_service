package com.paymentservice.backend.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.SuccessfulTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SuccessfulTransactionRepositoryImpl implements SuccessfulTransactionRepository {
    private static final Logger log = LoggerFactory.getLogger(SuccessfulTransactionRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(SuccessfulTransaction successfulTransaction) {
        try {
            entityManager.persist(successfulTransaction);
            log.info("Successfully saved successful transaction with ID: {}", successfulTransaction.getId());
        } catch (Exception e) {
            log.error("Error saving successful transaction with ID: {}. Error: {}", successfulTransaction.getId(), e.getMessage());
        }
    }
}
