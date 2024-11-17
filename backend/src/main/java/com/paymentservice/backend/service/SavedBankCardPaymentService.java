package com.paymentservice.backend.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.domain.SavedBankCard;
import com.paymentservice.backend.dto.SavedBankCardDto;
import com.paymentservice.backend.repository.SavedBankCardRepository;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedBankCardPaymentService {
    private static final Logger log = LoggerFactory.getLogger(SavedBankCardPaymentService.class);
    private final SavedBankCardRepository savedBankCardRepository;
    private final BankCardProcessingService bankCardProcessingService;

    @Transactional
    public BankCardPaymentResponse processPayment(SavedBankCardDto savedBankCardDto, BigDecimal paymentSum) {
        log.info("Payment with saved card for userId: {} with card number: {}", savedBankCardDto.getUserId(), savedBankCardDto.getCardNumber());
        SavedBankCard savedBankCard = savedBankCardRepository.findByUserIdAndCardNumber(savedBankCardDto.getUserId(),
                savedBankCardDto.getCardNumber());
        BankCardPaymentRequest bankCardPaymentRequest = new BankCardPaymentRequest(savedBankCard.getUserId(),
                savedBankCard.getCardNumber(), savedBankCard.getCvv(), paymentSum, savedBankCard.getExpiryDate());
        return bankCardProcessingService.processBankPayment(bankCardPaymentRequest, false);
    }
}
