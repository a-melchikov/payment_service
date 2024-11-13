package com.paymentservice.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.paymentservice.backend.domain.SavedBankCard;
import com.paymentservice.backend.dto.SavedBankCardDto;
import com.paymentservice.backend.repository.SavedBankCardRepository;
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
public class SavedBankCardPaymentServiceTest {

    @Mock
    private SavedBankCardRepository savedBankCardRepository;

    @Mock
    private BankCardProcessingService bankCardProcessingService;

    @InjectMocks
    private SavedBankCardPaymentService savedBankCardPaymentService;

    private SavedBankCard savedBankCard;
    private SavedBankCardDto savedBankCardDto;
    private BankCardPaymentResponse bankCardPaymentResponse;

    @BeforeEach
    public void setUp() {
        savedBankCard = new SavedBankCard();
        savedBankCard.setUserId(1L);
        savedBankCard.setCardNumber("1234567890123456");
        savedBankCard.setCvv("123");
        savedBankCard.setExpiryDate(LocalDate.of(2025, 12, 1));

        savedBankCardDto = new SavedBankCardDto();
        savedBankCardDto.setUserId(1L);
        savedBankCardDto.setCardNumber("1234567890123456");

        ResponseStatus successStatus = new ResponseStatus();
        successStatus.setStatus("Успех");
        bankCardPaymentResponse = new BankCardPaymentResponse();
        bankCardPaymentResponse.setResponseStatus(successStatus);
    }

    @Test
    public void testProcessPayment_Success() {
        BigDecimal paymentSum = BigDecimal.valueOf(1000.00);

        when(savedBankCardRepository.findByUserIdAndCardNumber(savedBankCardDto.getUserId(), savedBankCardDto.getCardNumber()))
                .thenReturn(savedBankCard);
        when(bankCardProcessingService.processBankPayment(any(BankCardPaymentRequest.class), eq(false)))
                .thenReturn(bankCardPaymentResponse);

        BankCardPaymentResponse response = savedBankCardPaymentService.processPayment(savedBankCardDto, paymentSum);

        assertEquals("Успех", response.getResponseStatus().getStatus());
        verify(savedBankCardRepository, times(1)).findByUserIdAndCardNumber(savedBankCardDto.getUserId(), savedBankCardDto.getCardNumber());
        verify(bankCardProcessingService, times(1)).processBankPayment(argThat(request ->
                request.getUserId().equals(savedBankCard.getUserId()) &&
                request.getCardNumber().equals(savedBankCard.getCardNumber()) &&
                request.getCvv().equals(savedBankCard.getCvv()) &&
                request.getPaymentSum().equals(paymentSum) &&
                request.getExpiryDate().equals(savedBankCard.getExpiryDate())
        ), eq(false));
    }

    @Test
    public void testProcessPayment_CardNotFound() {
        BigDecimal paymentSum = BigDecimal.valueOf(1000.00);

        when(savedBankCardRepository.findByUserIdAndCardNumber(savedBankCardDto.getUserId(), savedBankCardDto.getCardNumber()))
                .thenReturn(null);

        assertThrows(Exception.class, () -> {
            savedBankCardPaymentService.processPayment(savedBankCardDto, paymentSum);
        });

        verify(savedBankCardRepository, times(1)).findByUserIdAndCardNumber(savedBankCardDto.getUserId(), savedBankCardDto.getCardNumber());
        verify(bankCardProcessingService, never()).processBankPayment(any(BankCardPaymentRequest.class), anyBoolean());
    }
}
