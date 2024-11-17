package com.paymentservice.backend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

@Component
public class BankCardClient {
    private static final Logger log = LoggerFactory.getLogger(BankCardClient.class);
    private final WebClient webClient;

    public BankCardClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://172.17.0.1:8081/api/v1/bankcard").build();
    }

    public BankCardPaymentResponse makePayment(BankCardPaymentRequest paymentRequest) {
        log.info("Making payment request on bank API for userId: {} cardNumber {}", paymentRequest.getUserId(), paymentRequest.getCardNumber());
        return webClient.post()
                .uri("/pay")
                .bodyValue(paymentRequest)  
                .retrieve()               
                .bodyToMono(BankCardPaymentResponse.class)  
                .block();
    }
}
