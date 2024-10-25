package com.paymentservice.backend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.FailedTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class FailedTransactionRepositoryImpl implements FailedTransactionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(FailedTransaction failedTransaction) {
        entityManager.persist(failedTransaction);
    }
}
