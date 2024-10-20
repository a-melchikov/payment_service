package com.paymentservice.backend.service;

import com.paymentservice.backend.dto.BankCardPaymentRequest;
import com.paymentservice.backend.dto.BankCardPaymentResponse;

public interface BankCardProcessingService {
    BankCardPaymentResponse processBankPayment(BankCardPaymentRequest bankCardPaymentRequest);
}
