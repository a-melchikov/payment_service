DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'successful_transactions') THEN
        CREATE TABLE successful_transactions (
            transaction_id SERIAL PRIMARY KEY,
            payment_identifier VARCHAR(20),
            amount DECIMAL(10, 2) NOT NULL,
            transaction_date TIMESTAMP NOT NULL,
            user_id INT NOT NULL,
            payment_method VARCHAR(20) CHECK (payment_method IN ('card', 'yoomoney'))
        );
    END IF;
END $$;

CREATE OR REPLACE FUNCTION notify_successful_transaction()
RETURNS TRIGGER AS $$
BEGIN
  PERFORM pg_notify(
    'successful_transaction',
    json_build_object(
      'user_id', NEW.user_id,
      'transaction_id', NEW.transaction_id
    )::text
  );
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER successful_transaction_notify
AFTER INSERT ON successful_transactions
FOR EACH ROW
EXECUTE FUNCTION notify_successful_transaction();
