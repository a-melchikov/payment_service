package com.paymentservice.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentservice.backend.client.BankCardClient;
import com.paymentservice.backend.domain.FailedTransaction;
import com.paymentservice.backend.domain.SuccessfulTransaction;
import com.paymentservice.backend.repository.FailedTransactionRepository;
import com.paymentservice.backend.repository.SuccessfulTransactionRepository;
import com.paymentservice.backend.repository.UserRepository;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankCardProcessingServiceImpl implements BankCardProcessingService {
    private final FailedTransactionRepository failedTransactionRepository;
    private final SuccessfulTransactionRepository successfulTransactionRepository;
    private final UserRepository userRepository;
    private final BankCardClient bankCardClient;
    private final static String BANK_CARD = "card";
    private final static String SUCCESS = "Успех";

    @Override
    @Transactional
    public BankCardPaymentResponse processBankPayment(BankCardPaymentRequest bankCardPaymentRequest) {
        BankCardPaymentResponse bankCardPaymentResponse = bankCardClient.makePayment(bankCardPaymentRequest);
        if (SUCCESS.equals(bankCardPaymentResponse.getResponseStatus().getStatus())) {
            saveSuccessfulTransaction(bankCardPaymentRequest, bankCardPaymentResponse);
        } else {
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
        failedTransaction.setUser(userRepository.findRandomUser());
        failedTransactionRepository.save(failedTransaction);
    }

    private void saveSuccessfulTransaction(BankCardPaymentRequest bankCardPaymentRequest,
            BankCardPaymentResponse bankCardPaymentResponse) {
        SuccessfulTransaction successfulTransaction = new SuccessfulTransaction();
        successfulTransaction.setAmount(bankCardPaymentRequest.getPaymentSum());
        successfulTransaction.setPaymentIdentifier(bankCardPaymentRequest.getCardNumber());
        successfulTransaction.setPaymentMethod(BANK_CARD);
        successfulTransaction.setTransactionDate(LocalDateTime.now());
        successfulTransaction.setUser(userRepository.findRandomUser());
        successfulTransactionRepository.save(successfulTransaction);
    }
}
