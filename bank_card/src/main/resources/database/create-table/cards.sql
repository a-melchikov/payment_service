CREATE TABLE cards (
    card_id SERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL CHECK (LENGTH(card_number) = 16),
    card_holder_name VARCHAR(255) NOT NULL,
    expiry_date DATE NOT NULL,
    cvv VARCHAR(3) NOT NULL CHECK (LENGTH(cvv) = 3),
    balance DECIMAL(10, 2) NOT NULL CHECK (balance >= 0),
    card_type VARCHAR(20) CHECK (card_type IN ('VISA', 'MASTERCARD', 'МИР')),
    issuing_bank VARCHAR(50) NOT NULL
);