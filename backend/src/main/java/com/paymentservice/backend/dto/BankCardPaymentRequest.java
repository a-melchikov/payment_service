package com.paymentservice.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankCardPaymentRequest {
    private String cardNumber;
    private String cvv;
    private BigDecimal paymentSum;
    private LocalDate expiryDate;
}

