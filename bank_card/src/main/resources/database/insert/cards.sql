INSERT INTO cards (card_id, card_number, card_holder_name, expiry_date, cvv, balance, card_type, issuing_bank)
VALUES 
(1, '1234567812345678', 'John Doe', '2025-12-01', '123', 1000.00, 'VISA', 'Sberbank'),
(2, '8765432187654321', 'Jane Smith', '2024-11-01', '456', 500.50, 'MASTERCARD', 'Tinkoff'),
(3, '1122334455667788', 'Alex Brown', '2026-05-01', '789', 0.00, 'МИР', 'VTB'),
(4, '1111222233334444', 'Anna White', '2023-10-01', '111', 250.75, 'VISA', 'Alfa-Bank'),
(5, '5555666677778888', 'Michael Green', '2027-03-01', '222', 800.00, 'MASTERCARD', 'Raiffeisenbank'),
(6, '9999000011112222', 'Olga Black', '2025-07-01', '333', 120.00, 'МИР', 'Gazprombank'),
(7, '4444555566667777', 'Ivan Petrov', '2024-09-01', '444', 1500.00, 'VISA', 'Sberbank'),
(8, '8888999900001111', 'Svetlana Ivanova', '2028-01-01', '555', 950.30, 'MASTERCARD', 'Tinkoff'),
(9, '3333444455556666', 'Pavel Popov', '2023-12-01', '666', 400.00, 'МИР', 'VTB'),
(10, '6666777788889999', 'Natalia Sokolova', '2025-05-01', '777', 300.00, 'VISA', 'Alfa-Bank'),
(11, '2222333344445555', 'Dmitry Orlov', '2026-11-01', '888', 600.00, 'MASTERCARD', 'Raiffeisenbank'),
(12, '7777888899990000', 'Elena Kolesnikova', '2025-02-01', '999', 1050.00, 'МИР', 'Gazprombank')
ON CONFLICT(card_id) DO NOTHING;