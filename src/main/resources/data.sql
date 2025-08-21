-- Uwaga: jeśli już masz dane, dopasuj / pomiń.
INSERT INTO roles(name) VALUES ('ADMIN'), ('USER');

INSERT INTO users(username,password,enabled) VALUES
 ('admin', '{bcrypt}$2a$10$AxY6TqjQkzJtQ6XG/Kf8..Z3aM4RF5hQJZbS4jQW77QvSM3jFyv7K', true); 
-- Hasło: admin (zmień w produkcji!)

INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username='admin' AND r.name='ADMIN';