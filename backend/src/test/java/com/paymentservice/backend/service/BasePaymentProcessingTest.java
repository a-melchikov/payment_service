package com.paymentservice.backend.service;

import com.paymentservice.backend.client.BankCardClient;
import com.paymentservice.backend.repository.FailedTransactionRepository;
import com.paymentservice.backend.repository.SuccessfulTransactionRepository;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;
import com.paymentservice.dto.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BankCardProcessingServiceImplTest {

    @Autowired
    private BankCardProcessingServiceImpl bankCardProcessingService;

    @Autowired
    private FailedTransactionRepository failedTransactionRepository;

    @Autowired
    private SuccessfulTransactionRepository successfulTransactionRepository;

    @Autowired
    private SavedBankCardService savedBankCardService;

    @Autowired
    private BankCardClient bankCardClient;

    private BankCardPaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        // Подготовим тестовые данные
        paymentRequest = new BankCardPaymentRequest();
        paymentRequest.setCardNumber("1234567812345678");
        paymentRequest.setCvv("123");

        // Преобразуем строку в LocalDate (предположим, что месяц/год)
        paymentRequest.setExpiryDate(LocalDate.of(2025, 12, 1)); // или используйте parse из строки

        paymentRequest.setPaymentSum(new BigDecimal("1000.00")); // Используем BigDecimal для точности
        paymentRequest.setUserId(1L);
    }

    @Test
    void processBankPayment_SuccessfulTransaction() {
        // Настроим response от bankCardClient
        BankCardPaymentResponse paymentResponse = new BankCardPaymentResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus("Успех");
        paymentResponse.setResponseStatus(responseStatus);

        // Переопределим метод bankCardClient.makePayment для имитации успешного ответа
        bankCardClient = new BankCardClient(null) {
            @Override
            public BankCardPaymentResponse makePayment(BankCardPaymentRequest request) {
                return paymentResponse;
            }
        };

        // Выполняем платеж
        BankCardPaymentResponse result = bankCardProcessingService.processBankPayment(paymentRequest, true);

        // Проверяем результат
        assertNotNull(result);
        assertEquals("Успех", result.getResponseStatus().getStatus());
    }

    @Test
    void processBankPayment_FailedTransaction() {
        // Настроим response от bankCardClient для ошибки
        BankCardPaymentResponse paymentResponse = new BankCardPaymentResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus("Ошибка");
        paymentResponse.setResponseStatus(responseStatus);

        // Переопределим метод bankCardClient.makePayment для имитации неудачного ответа
        bankCardClient = new BankCardClient(null) {
            @Override
            public BankCardPaymentResponse makePayment(BankCardPaymentRequest request) {
                return paymentResponse;
            }
        };

        // Выполняем платеж
        BankCardPaymentResponse result = bankCardProcessingService.processBankPayment(paymentRequest, false);

        // Проверяем результат
        assertNotNull(result);
        assertEquals("Ошибка", result.getResponseStatus().getStatus());
    }

    @Test
    void processBankPayment_ShouldSaveBankCard() {
        // Настроим response от bankCardClient
        BankCardPaymentResponse paymentResponse = new BankCardPaymentResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus("Успех");
        paymentResponse.setResponseStatus(responseStatus);

        // Переопределим метод bankCardClient.makePayment для имитации успешного ответа
        bankCardClient = new BankCardClient(null) {
            @Override
            public BankCardPaymentResponse makePayment(BankCardPaymentRequest request) {
                return paymentResponse;
            }
        };

        // Выполняем платеж с флагом shouldSave = true, чтобы сохранить карту
        BankCardPaymentResponse result = bankCardProcessingService.processBankPayment(paymentRequest, true);
    }
}
