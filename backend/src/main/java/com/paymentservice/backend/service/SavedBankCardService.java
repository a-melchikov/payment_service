package com.paymentservice.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.SavedBankCard;
import com.paymentservice.backend.dto.SavedBankCardDto;
import com.paymentservice.backend.repository.SavedBankCardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedBankCardService {
    private final SavedBankCardRepository savedBankCardRepository;

    public List<SavedBankCardDto> getBankCardsByUserId(Long userId) {
        return savedBankCardRepository.findSavedBankCardsByUserId(userId).stream().map(savedCard -> savedCard.toDto())
                .toList();
    }

    @Transactional
    public void save(SavedBankCard savedBankCard) {
        if (!savedBankCardRepository.isCardExists(savedBankCard.getUserId(), savedBankCard.getCardNumber())) {
            savedBankCardRepository.save(savedBankCard);
        }
    }

    @Transactional
    public void delete(SavedBankCardDto savedBankCardDto) {
        SavedBankCard savedBankCard = savedBankCardRepository.findByUserIdAndCardNumber(savedBankCardDto.getUserId(),
                savedBankCardDto.getCardNumber());
        if (savedBankCard != null) {
            savedBankCardRepository.delete(savedBankCard);
        }
    }
}
