package com.paymentservice.backend.repository;

import com.paymentservice.backend.domain.SuccessfulTransaction;

public interface SuccessfulTransactionRepository {
    void save(SuccessfulTransaction successfulTransaction);
}
