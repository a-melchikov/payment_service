package com.paymentservice.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.SavedBankCard;
import com.paymentservice.backend.dto.SavedBankCardDto;
import com.paymentservice.backend.repository.SavedBankCardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedBankCardService {
    private static final Logger log = LoggerFactory.getLogger(SavedBankCardService.class);
    private final SavedBankCardRepository savedBankCardRepository;

    public List<SavedBankCardDto> getBankCardsByUserId(Long userId) {
        List<SavedBankCardDto> savedBankCards = savedBankCardRepository.findSavedBankCardsByUserId(userId).stream()
                .map(SavedBankCard::toDto)
                .toList();
        log.info("Fetched {} saved bank cards for userId: {}", savedBankCards.size(), userId);
        return savedBankCards;
    }

    @Transactional
    public void save(SavedBankCard savedBankCard) {
        if (!savedBankCardRepository.isCardExists(savedBankCard.getUserId(), savedBankCard.getCardNumber())) {
            savedBankCardRepository.save(savedBankCard);
            log.info("Successfully saved bank card for userId: {}, cardNumber: {}", savedBankCard.getUserId(), savedBankCard.getCardNumber());
        } else {
            log.warn("Bank card already exists for userId: {}, cardNumber: {}", savedBankCard.getUserId(), savedBankCard.getCardNumber());
        }
    }

    @Transactional
    public void delete(SavedBankCardDto savedBankCardDto) {
        SavedBankCard savedBankCard = savedBankCardRepository.findByUserIdAndCardNumber(savedBankCardDto.getUserId(),
                savedBankCardDto.getCardNumber());
        if (savedBankCard != null) {
            savedBankCardRepository.delete(savedBankCard);
            log.info("Successfully deleted saved bank card for userId: {}, cardNumber: {}", savedBankCardDto.getUserId(), savedBankCardDto.getCardNumber());
        } else {
            log.warn("Bank card not found for userId: {}, cardNumber: {}", savedBankCardDto.getUserId(), savedBankCardDto.getCardNumber());
        }
    }
}
