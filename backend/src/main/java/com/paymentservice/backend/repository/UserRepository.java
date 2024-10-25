package com.paymentservice.backend.repository;

import com.paymentservice.backend.domain.User;

public interface UserRepository {
    User findRandomUser();
}
