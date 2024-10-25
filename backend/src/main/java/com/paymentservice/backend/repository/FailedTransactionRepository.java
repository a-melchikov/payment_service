package com.paymentservice.backend.repository;

import com.paymentservice.backend.domain.FailedTransaction;

public interface FailedTransactionRepository {
    void save(FailedTransaction failedTransaction);
}
