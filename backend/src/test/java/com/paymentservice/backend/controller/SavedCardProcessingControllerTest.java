package com.paymentservice.backend.controller;

import com.paymentservice.backend.dto.SavedBankCardDto;
import com.paymentservice.backend.service.SavedBankCardService;
import com.paymentservice.backend.service.SavedBankCardPaymentService;
import com.paymentservice.dto.BankCardPaymentResponse;
import com.paymentservice.dto.ResponseStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SavedCardProcessingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SavedBankCardService savedBankCardService;

    @Mock
    private SavedBankCardPaymentService savedBankCardPaymentService;

    private SavedCardProcessingController savedCardProcessingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        savedCardProcessingController = new SavedCardProcessingController(savedBankCardService, savedBankCardPaymentService);

        mockMvc = MockMvcBuilders.standaloneSetup(savedCardProcessingController).build();
    }

    @Test
    public void testGetSavedCardByUserId_Success() throws Exception {
        SavedBankCardDto savedBankCard = new SavedBankCardDto();
        savedBankCard.setCardNumber("1234567890123456");
        
        List<SavedBankCardDto> savedCards = Collections.singletonList(savedBankCard);
        when(savedBankCardService.getBankCardsByUserId(anyLong())).thenReturn(savedCards);

        mockMvc.perform(get("/api/v1/saved-cards/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890123456"));
        
        verify(savedBankCardService, times(1)).getBankCardsByUserId(anyLong());
    }

    @Test
    public void testDeleteSavedCard_Success() throws Exception {
        SavedBankCardDto savedBankCard = new SavedBankCardDto();
        savedBankCard.setCardNumber("1234567890123456");

        mockMvc.perform(delete("/api/v1/saved-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\": \"1234567890123456\"}"))
                .andExpect(status().isOk());
        
        verify(savedBankCardService, times(1)).delete(any(SavedBankCardDto.class));
    }

    @Test
    public void testPay_Success() throws Exception {
        SavedBankCardDto savedBankCard = new SavedBankCardDto();
        savedBankCard.setCardNumber("1234567890123456");
        
        BankCardPaymentResponse paymentResponse = new BankCardPaymentResponse();
        paymentResponse.setResponseStatus(new ResponseStatus("Успех", "..."));

        when(savedBankCardPaymentService.processPayment(any(SavedBankCardDto.class), any(BigDecimal.class)))
                .thenReturn(paymentResponse);

        mockMvc.perform(post("/api/v1/saved-cards/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\": \"1234567890123456\"}")
                        .param("paymentSum", "100.00"))
                .andExpect(status().isOk());
        
        verify(savedBankCardPaymentService, times(1)).processPayment(any(SavedBankCardDto.class), any(BigDecimal.class));
    }
}
