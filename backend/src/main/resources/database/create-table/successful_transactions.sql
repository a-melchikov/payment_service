CREATE TABLE successful_transactions (
    transaction_id SERIAL PRIMARY KEY,
    card_id INT REFERENCES cards(card_id),
    amount DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    user_id INT REFERENCES users(user_id),
    description TEXT,
    payment_method VARCHAR(20) CHECK (payment_method IN ('card', 'yoomoney'))
);