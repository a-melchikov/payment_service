package com.paymentservice.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import com.paymentservice.backend.domain.SavedBankCard;
import com.paymentservice.backend.dto.SavedBankCardDto;
import com.paymentservice.backend.repository.SavedBankCardRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SavedBankCardServiceTest {

    @Mock
    private SavedBankCardRepository savedBankCardRepository;

    @InjectMocks
    private SavedBankCardService savedBankCardService;

    private SavedBankCard savedBankCard;
    private SavedBankCardDto savedBankCardDto;

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
    }

    @Test
    public void testGetBankCardsByUserId() {
        when(savedBankCardRepository.findSavedBankCardsByUserId(1L)).thenReturn(List.of(savedBankCard));
        List<SavedBankCardDto> result = savedBankCardService.getBankCardsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(savedBankCard.getCardNumber(), result.get(0).getCardNumber());
        verify(savedBankCardRepository, times(1)).findSavedBankCardsByUserId(1L);
    }

    @Test
    public void testSave_CardDoesNotExist() {
        when(savedBankCardRepository.isCardExists(1L, "1234567890123456")).thenReturn(false);

        savedBankCardService.save(savedBankCard);

        verify(savedBankCardRepository, times(1)).isCardExists(1L, "1234567890123456");
        verify(savedBankCardRepository, times(1)).save(savedBankCard);
    }

    @Test
    public void testSave_CardAlreadyExists() {
        when(savedBankCardRepository.isCardExists(1L, "1234567890123456")).thenReturn(true);

        savedBankCardService.save(savedBankCard);

        verify(savedBankCardRepository, times(1)).isCardExists(1L, "1234567890123456");
        verify(savedBankCardRepository, never()).save(any(SavedBankCard.class));
    }

    @Test
    public void testDelete_CardExists() {
        when(savedBankCardRepository.findByUserIdAndCardNumber(1L, "1234567890123456")).thenReturn(savedBankCard);

        savedBankCardService.delete(savedBankCardDto);

        verify(savedBankCardRepository, times(1)).findByUserIdAndCardNumber(1L, "1234567890123456");
        verify(savedBankCardRepository, times(1)).delete(savedBankCard);
    }

    @Test
    public void testDelete_CardDoesNotExist() {
        when(savedBankCardRepository.findByUserIdAndCardNumber(1L, "1234567890123456")).thenReturn(null);

        savedBankCardService.delete(savedBankCardDto);

        verify(savedBankCardRepository, times(1)).findByUserIdAndCardNumber(1L, "1234567890123456");
        verify(savedBankCardRepository, never()).delete(any(SavedBankCard.class));
    }
}
