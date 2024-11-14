package com.paymentservice.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavedBankCardDto {
    private String cardNumber;
    private Long userId;
    private String issuingBank;
}
