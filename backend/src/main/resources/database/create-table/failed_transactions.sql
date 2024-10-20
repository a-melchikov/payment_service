CREATE TABLE failed_transactions (
    transaction_id SERIAL PRIMARY KEY,
    payment_identifier VARCHAR(20),
    amount DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    user_id INT REFERENCES users(user_id),
    error_message TEXT NOT NULL,
    payment_method VARCHAR(20) CHECK (payment_method IN ('card', 'yoomoney'))
);