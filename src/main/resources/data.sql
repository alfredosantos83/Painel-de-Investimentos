-- Script de inicialização do banco de dados (H2/Panache)
-- Senha para todos usuários: password123

-- Inserir usuários de teste (usando sequência)
INSERT INTO users (id, username, password, email, role, enabled) VALUES
(NEXT VALUE FOR users_SEQ, 'admin', '$2a$12$k7ebzNvmKCDiKFwxZX0yhueJOtxxfjOL8/Q6rw1rcwobieWCc3Y7S', 'admin@caixa.com', 'ADMIN', true),
(NEXT VALUE FOR users_SEQ, 'user', '$2a$12$k7ebzNvmKCDiKFwxZX0yhueJOtxxfjOL8/Q6rw1rcwobieWCc3Y7S', 'user@caixa.com', 'USER', true);
