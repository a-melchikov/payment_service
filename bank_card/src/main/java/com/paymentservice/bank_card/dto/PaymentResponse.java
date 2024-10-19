package com.paymentservice.bank_card.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String cardNumber;
    private BigDecimal currentBalance;
    private String message;
}
