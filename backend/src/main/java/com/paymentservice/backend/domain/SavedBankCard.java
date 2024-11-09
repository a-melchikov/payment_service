package com.paymentservice.backend.domain;

import java.time.LocalDate;

import com.paymentservice.backend.dto.SavedBankCardDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "saved_bank_cards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavedBankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", length = 16, nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "cvv", length = 3, nullable = false)
    private String cvv;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public SavedBankCardDto toDto() {
        return new SavedBankCardDto(cardNumber, userId);
    }
}
