package com.paymentservice.bank_card.service;

import com.paymentservice.bank_card.dto.PaymentRequest;
import com.paymentservice.bank_card.dto.PaymentResponse;

public interface CardService {
    PaymentResponse makePayment(PaymentRequest paymentRequest);
}
