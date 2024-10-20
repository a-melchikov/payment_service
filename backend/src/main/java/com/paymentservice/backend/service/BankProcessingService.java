package com.paymentservice.backend.service;

import com.paymentservice.backend.dto.BankCardPaymentRequest;
import com.paymentservice.backend.dto.BankCardPaymentResponse;

public interface BankProcessingService {
    BankCardPaymentResponse processBankPayment(BankCardPaymentRequest bankCardPaymentRequest);
}
