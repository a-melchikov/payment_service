package com.paymentservice.bank_card.service;

import com.paymentservice.bank_card.dto.BankCardPaymentRequest;
import com.paymentservice.bank_card.dto.BankCardPaymentResponse;

public interface CardService {
    BankCardPaymentResponse pay(BankCardPaymentRequest paymentRequest);
}
