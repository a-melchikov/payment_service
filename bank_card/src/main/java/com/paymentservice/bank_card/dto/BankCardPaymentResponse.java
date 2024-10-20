package com.paymentservice.bank_card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankCardPaymentResponse {
    private String cardNumber;
    private ResponseStatus responseStatus;
}
