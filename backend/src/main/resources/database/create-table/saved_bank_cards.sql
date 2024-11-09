CREATE TABLE IF NOT EXISTS saved_bank_cards (
    id SERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL CHECK (LENGTH(card_number) = 16),
    expiry_date DATE NOT NULL,
    cvv VARCHAR(3) NOT NULL CHECK (LENGTH(cvv) = 3),
    user_id INT NOT NULL
);