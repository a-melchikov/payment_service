package com.paymentservice.backend.repository;

import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.SuccessfulTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class SuccessfulTransactionRepositoryImpl implements SuccessfulTransactionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(SuccessfulTransaction successfulTransaction) {
        entityManager.persist(successfulTransaction);
    }
}
