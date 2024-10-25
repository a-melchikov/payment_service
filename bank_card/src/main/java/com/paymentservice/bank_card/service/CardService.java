package com.paymentservice.bank_card.service;

import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

public interface CardService {
    BankCardPaymentResponse pay(BankCardPaymentRequest paymentRequest);
}
