package com.paymentservice.backend.service;

import java.math.BigDecimal;

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
    private final SavedBankCardRepository savedBankCardRepository;
    private final BankCardProcessingService bankCardProcessingService;

    @Transactional
    public BankCardPaymentResponse processPayment(SavedBankCardDto savedBankCardDto, BigDecimal paymentSum) {
        SavedBankCard savedBankCard = savedBankCardRepository.findByUserIdAndCardNumber(savedBankCardDto.getUserId(),
                savedBankCardDto.getCardNumber());
        BankCardPaymentRequest bankCardPaymentRequest = new BankCardPaymentRequest(savedBankCard.getUserId(),
                savedBankCard.getCardNumber(), savedBankCard.getCvv(), paymentSum, savedBankCard.getExpiryDate());
        return bankCardProcessingService.processBankPayment(bankCardPaymentRequest, false);
    }
}
