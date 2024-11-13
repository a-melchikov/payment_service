package com.paymentservice.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.paymentservice.backend.client.BankCardClient;
import com.paymentservice.backend.domain.FailedTransaction;
import com.paymentservice.backend.domain.SuccessfulTransaction;
import com.paymentservice.backend.repository.FailedTransactionRepository;
import com.paymentservice.backend.repository.SuccessfulTransactionRepository;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;
import com.paymentservice.dto.ResponseStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BankCardProcessingServiceImplTest {

    @Mock
    private FailedTransactionRepository failedTransactionRepository;

    @Mock
    private SuccessfulTransactionRepository successfulTransactionRepository;

    @Mock
    private SavedBankCardService savedBankCardService;

    @Mock
    private BankCardClient bankCardClient;

    @InjectMocks
    private BankCardProcessingServiceImpl bankCardProcessingService;

    private BankCardPaymentRequest paymentRequest;
    private BankCardPaymentResponse successfulResponse;
    private BankCardPaymentResponse failedResponse;

    @BeforeEach
    public void setUp() {
        paymentRequest = new BankCardPaymentRequest();
        paymentRequest.setCardNumber("1234567890123456");
        paymentRequest.setCvv("123");
        paymentRequest.setExpiryDate(LocalDate.of(2025, 12, 1));
        paymentRequest.setPaymentSum(BigDecimal.valueOf(1000.0));
        paymentRequest.setUserId(1L);

        ResponseStatus successStatus = new ResponseStatus();
        successStatus.setStatus("Успех");
        successfulResponse = new BankCardPaymentResponse();
        successfulResponse.setResponseStatus(successStatus);

        ResponseStatus failedStatus = new ResponseStatus();
        failedStatus.setStatus("Ошибка");
        failedStatus.setMessage("Недостаточно средств");
        failedResponse = new BankCardPaymentResponse();
        failedResponse.setResponseStatus(failedStatus);
    }

    @Test
    public void testProcessBankPayment_Success() {
        when(bankCardClient.makePayment(paymentRequest)).thenReturn(successfulResponse);

        BankCardPaymentResponse response = bankCardProcessingService.processBankPayment(paymentRequest, true);

        assertEquals("Успех", response.getResponseStatus().getStatus());
        verify(successfulTransactionRepository, times(1)).save(any(SuccessfulTransaction.class));
        verify(savedBankCardService, times(1)).save(any());
        verify(failedTransactionRepository, never()).save(any(FailedTransaction.class));
    }

    @Test
    public void testProcessBankPayment_Failed() {
        when(bankCardClient.makePayment(paymentRequest)).thenReturn(failedResponse);

        BankCardPaymentResponse response = bankCardProcessingService.processBankPayment(paymentRequest, false);

        assertEquals("Ошибка", response.getResponseStatus().getStatus());
        verify(failedTransactionRepository, times(1)).save(any(FailedTransaction.class));
        verify(successfulTransactionRepository, never()).save(any(SuccessfulTransaction.class));
        verify(savedBankCardService, never()).save(any());
    }

    @Test
    public void testSaveFailedTransaction() {
        when(bankCardClient.makePayment(paymentRequest)).thenReturn(failedResponse);

        bankCardProcessingService.processBankPayment(paymentRequest, false);

        verify(failedTransactionRepository, times(1)).save(argThat(failedTransaction -> 
            failedTransaction.getAmount().equals(paymentRequest.getPaymentSum()) &&
            failedTransaction.getPaymentIdentifier().equals(paymentRequest.getCardNumber()) &&
            failedTransaction.getUserId().equals(paymentRequest.getUserId())
        ));
    }

    @Test
    public void testSaveSuccessfulTransaction() {
        when(bankCardClient.makePayment(paymentRequest)).thenReturn(successfulResponse);

        bankCardProcessingService.processBankPayment(paymentRequest, false);

        verify(successfulTransactionRepository, times(1)).save(argThat(successfulTransaction -> 
            successfulTransaction.getAmount().equals(paymentRequest.getPaymentSum()) &&
            successfulTransaction.getPaymentIdentifier().equals(paymentRequest.getCardNumber()) &&
            successfulTransaction.getUserId().equals(paymentRequest.getUserId())
        ));
    }

    @Test
    public void testSaveBankCard() {
        when(bankCardClient.makePayment(paymentRequest)).thenReturn(successfulResponse);

        bankCardProcessingService.processBankPayment(paymentRequest, true);

        verify(savedBankCardService, times(1)).save(argThat(savedBankCard -> 
            savedBankCard.getCardNumber().equals(paymentRequest.getCardNumber()) &&
            savedBankCard.getCvv().equals(paymentRequest.getCvv()) &&
            savedBankCard.getExpiryDate().equals(paymentRequest.getExpiryDate()) &&
            savedBankCard.getUserId().equals(paymentRequest.getUserId())
        ));
    }
}
