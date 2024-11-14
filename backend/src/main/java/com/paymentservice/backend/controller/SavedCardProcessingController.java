package com.paymentservice.backend.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymentservice.backend.dto.SavedBankCardDto;
import com.paymentservice.backend.service.SavedBankCardService;
import com.paymentservice.backend.service.SavedBankCardPaymentService;
import com.paymentservice.dto.BankCardPaymentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/saved-cards")
@CrossOrigin
@Tag(name = "Saved Cards API", description = "API для работы с сохраненными банковскими картами")
public class SavedCardProcessingController {
    private final SavedBankCardService savedBankCardService;
    private final SavedBankCardPaymentService savedBankcardPaymentService;

    @GetMapping("/{userId}")
    @Operation(summary = "Получить сохраненные банковские карты")
    public List<SavedBankCardDto> getSavedCardByUserId(@RequestParam(value = "userId") String userId) {
        return savedBankCardService.getBankCardsByUserId(Long.valueOf(userId));
    }

    @DeleteMapping
    @Operation(summary = "Удалить сохраненную банковскую карту")
    public void deleteSavedCard(@RequestBody SavedBankCardDto savedBankCardDto,
                                @RequestParam(value = "userId") String userId) {
        savedBankCardService.delete(savedBankCardDto);
    }

    @PostMapping("/pay")
    @Operation(summary = "Осуществить платеж по сохраненной банковской карты")
    public BankCardPaymentResponse pay(@RequestBody SavedBankCardDto savedBankCardDto,
            @RequestParam(value = "paymentSum") BigDecimal paymentSum,
            @RequestParam(value = "userId") String userId) {
        return savedBankcardPaymentService.processPayment(savedBankCardDto, paymentSum);
    }
}
