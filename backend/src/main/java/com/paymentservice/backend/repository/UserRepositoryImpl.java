package com.paymentservice.backend.repository;

import org.springframework.stereotype.Repository;

import com.paymentservice.backend.domain.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public User findRandomUser() {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM users ORDER BY RANDOM() LIMIT 1", User.class);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new RuntimeException("No user found", e);
        }
    }
}
