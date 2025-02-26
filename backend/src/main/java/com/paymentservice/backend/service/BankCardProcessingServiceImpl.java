package com.paymentservice.backend.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.client.BankCardClient;
import com.paymentservice.backend.domain.FailedTransaction;
import com.paymentservice.backend.domain.SavedBankCard;
import com.paymentservice.backend.domain.SuccessfulTransaction;
import com.paymentservice.backend.repository.FailedTransactionRepository;
import com.paymentservice.backend.repository.SuccessfulTransactionRepository;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankCardProcessingServiceImpl implements BankCardProcessingService {
    private static final Logger log = LoggerFactory.getLogger(BankCardProcessingServiceImpl.class);
    private final FailedTransactionRepository failedTransactionRepository;
    private final SuccessfulTransactionRepository successfulTransactionRepository;
    private final SavedBankCardService savedBankCardService;
    private final BankCardClient bankCardClient;
    private final static String BANK_CARD = "card";
    private final static String SUCCESS = "Успех";

    @Override
    @Transactional
    public BankCardPaymentResponse processBankPayment(BankCardPaymentRequest bankCardPaymentRequest, boolean shouldSave) {
        log.info("Start processing payment for userId: {}, cardNumber: {}", bankCardPaymentRequest.getUserId(), bankCardPaymentRequest.getCardNumber());
        BankCardPaymentResponse bankCardPaymentResponse = bankCardClient.makePayment(bankCardPaymentRequest);
        if (SUCCESS.equals(bankCardPaymentResponse.getResponseStatus().getStatus())) {
            log.info("Payment successful for userId: {}, cardNumber: {}", bankCardPaymentRequest.getUserId(), bankCardPaymentRequest.getCardNumber());
            saveSuccessfulTransaction(bankCardPaymentRequest, bankCardPaymentResponse);
            if (shouldSave) {
                log.info("Saving card information for userId: {}", bankCardPaymentRequest.getUserId());
                saveBankCard(bankCardPaymentRequest);
            }
        } else {
            log.info("Payment failed for userId: {}, cardNumber: {}. Error: {}",
                    bankCardPaymentRequest.getUserId(), bankCardPaymentRequest.getCardNumber(),
                    bankCardPaymentResponse.getResponseStatus().getMessage());
            saveFailedTransaction(bankCardPaymentRequest, bankCardPaymentResponse);
        }
        return bankCardPaymentResponse;
    }

    private void saveFailedTransaction(BankCardPaymentRequest bankCardPaymentRequest,
            BankCardPaymentResponse bankCardPaymentResponse) {
        FailedTransaction failedTransaction = new FailedTransaction();
        failedTransaction.setAmount(bankCardPaymentRequest.getPaymentSum());
        failedTransaction.setPaymentIdentifier(bankCardPaymentRequest.getCardNumber());
        failedTransaction.setErrorMessage(bankCardPaymentResponse.getResponseStatus().getMessage());
        failedTransaction.setTransactionDate(LocalDateTime.now());
        failedTransaction.setPaymentMethod(BANK_CARD);
        failedTransaction.setUserId(bankCardPaymentRequest.getUserId());
        failedTransactionRepository.save(failedTransaction);
    }

    private void saveSuccessfulTransaction(BankCardPaymentRequest bankCardPaymentRequest,
            BankCardPaymentResponse bankCardPaymentResponse) {
        SuccessfulTransaction successfulTransaction = new SuccessfulTransaction();
        successfulTransaction.setAmount(bankCardPaymentRequest.getPaymentSum());
        successfulTransaction.setPaymentIdentifier(bankCardPaymentRequest.getCardNumber());
        successfulTransaction.setPaymentMethod(BANK_CARD);
        successfulTransaction.setTransactionDate(LocalDateTime.now());
        successfulTransaction.setUserId(bankCardPaymentRequest.getUserId());
        successfulTransactionRepository.save(successfulTransaction);
    }

    private void saveBankCard(BankCardPaymentRequest bankCardPaymentRequest) {
        SavedBankCard savedBankCard = new SavedBankCard();
        savedBankCard.setCardNumber(bankCardPaymentRequest.getCardNumber());
        savedBankCard.setCvv(bankCardPaymentRequest.getCvv());
        savedBankCard.setExpiryDate(bankCardPaymentRequest.getExpiryDate());
        savedBankCard.setUserId(bankCardPaymentRequest.getUserId());
        savedBankCardService.save(savedBankCard);
    }
}
