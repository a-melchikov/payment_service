package com.paymentservice.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import com.paymentservice.backend.service.BankCardProcessingService;
import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;
import com.paymentservice.dto.ResponseStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BasePaymentProcessingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BankCardProcessingService bankProcessingService;

    private BasePaymentProcessingController basePaymentProcessingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        basePaymentProcessingController = new BasePaymentProcessingController(bankProcessingService);

        mockMvc = MockMvcBuilders.standaloneSetup(basePaymentProcessingController).build();
    }

    @Test
    public void testPay_Success() throws Exception {
        BankCardPaymentRequest request = new BankCardPaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setExpiryDate(LocalDate.now().plusYears(1));
        request.setCvv("123");

        BankCardPaymentResponse response = new BankCardPaymentResponse();
        response.setResponseStatus(new ResponseStatus("Успех", "успех"));

        when(bankProcessingService.processBankPayment(any(BankCardPaymentRequest.class), anyBoolean()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/payments/bankcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\": \"1234567890123456\", \"expiryDate\": \"2025-12-31\", \"cvv\": \"123\"}")
                        .param("shouldSave", "true"))
                .andExpect(status().isOk());
    }
}


