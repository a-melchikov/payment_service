package com.paymentservice.backend.repository;

import java.util.List;

import com.paymentservice.backend.domain.SavedBankCard;

public interface SavedBankCardRepository {
    void save(SavedBankCard savedBankCard);

    void delete(SavedBankCard savedBankCard);

    List<SavedBankCard> findSavedBankCardsByUserId(Long userId);

    SavedBankCard findByUserIdAndCardNumber(Long userId, String cardNumber);
}
