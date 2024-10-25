package com.paymentservice.dto;

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
