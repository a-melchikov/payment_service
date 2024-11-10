package com.paymentservice.backend.service;

import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

public interface BankCardProcessingService {
    BankCardPaymentResponse processBankPayment(BankCardPaymentRequest bankCardPaymentRequest, boolean shouldSave);
}
