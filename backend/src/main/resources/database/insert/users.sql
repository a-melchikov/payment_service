INSERT INTO users (user_id, email, login)
VALUES 
(1, 'john.doe@example.com', 'johndoe'),
(2, 'jane.smith@example.com', 'janesmith'),
(3, 'alex.brown@example.com', 'alexbrown')
ON CONFLICT(user_id) DO NOTHING;