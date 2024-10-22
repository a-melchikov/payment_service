package com.paymentservice.backend.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;

@Component
public class BankCardClient {
    private final WebClient webClient;

    public BankCardClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://bank-card-app:8081/api/v1/bankcard").build();
    }

    public BankCardPaymentResponse makePayment(BankCardPaymentRequest paymentRequest) {
        return webClient.post()
                .uri("/pay")
                .bodyValue(paymentRequest)  
                .retrieve()               
                .bodyToMono(BankCardPaymentResponse.class)  
                .block();
    }
}
