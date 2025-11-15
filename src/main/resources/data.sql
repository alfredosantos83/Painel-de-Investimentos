-- Script de inicialização do banco de dados

-- Inserir usuários de teste
INSERT INTO users (username, password, email, role, enabled) VALUES
('admin', '$2a$10$7PtcjEnWb/ZkgyXyxY0.GuLMtq3FxXz0r6Y1TWzRMcLsGHNPEPkg.', 'admin@caixa.com', 'ADMIN', true),
('user', '$2a$10$7PtcjEnWb/ZkgyXyxY0.GuLMtq3FxXz0r6Y1TWzRMcLsGHNPEPkg.', 'user@caixa.com', 'USER', true);
-- Senha para ambos: password123

-- Inserir produtos de investimento
INSERT INTO products (nome, tipo, rentabilidade, risco, prazo_minimo_meses, prazo_maximo_meses, valor_minimo, valor_maximo, liquidez_dias, ativo, descricao) VALUES
('CDB Caixa 2026', 'CDB', 0.12, 'BAIXO', 6, 24, 1000.00, 1000000.00, 90, true, 'CDB com liquidez trimestral e rentabilidade de 12% a.a.'),
('CDB Premium', 'CDB', 0.135, 'BAIXO', 12, 36, 5000.00, 5000000.00, 180, true, 'CDB para investimentos acima de R$ 5.000'),
('LCI Imobiliária', 'LCI', 0.10, 'BAIXO', 12, 24, 3000.00, 2000000.00, 365, true, 'Letra de Crédito Imobiliário isenta de IR'),
('LCA Agrícola', 'LCA', 0.095, 'BAIXO', 12, 24, 3000.00, 2000000.00, 365, true, 'Letra de Crédito do Agronegócio isenta de IR'),
('Tesouro Selic 2027', 'TESOURO_DIRETO', 0.115, 'BAIXO', 1, 60, 100.00, 10000000.00, 1, true, 'Tesouro Direto com liquidez diária'),
('Tesouro Prefixado 2029', 'TESOURO_DIRETO', 0.125, 'MEDIO', 12, 84, 100.00, 10000000.00, 365, true, 'Tesouro Prefixado com vencimento em 2029'),
('Fundo DI Referenciado', 'FUNDO_RENDA_FIXA', 0.11, 'BAIXO', 1, 120, 500.00, 50000000.00, 1, true, 'Fundo que acompanha a variação do CDI'),
('Fundo Renda Fixa Ativo', 'FUNDO_RENDA_FIXA', 0.13, 'MEDIO', 6, 120, 1000.00, 50000000.00, 30, true, 'Fundo de renda fixa com gestão ativa'),
('Fundo Multimercado', 'FUNDO_MULTIMERCADO', 0.18, 'ALTO', 12, 120, 5000.00, 100000000.00, 90, true, 'Fundo que investe em múltiplas classes de ativos'),
('Fundo de Ações Ibovespa', 'FUNDO_ACOES', 0.20, 'ALTO', 12, 120, 1000.00, 100000000.00, 30, true, 'Fundo que replica o índice Ibovespa'),
('Poupança', 'POUPANCA', 0.07, 'BAIXO', 1, 360, 10.00, 100000000.00, 1, true, 'Caderneta de poupança tradicional');

-- Inserir clientes de exemplo
INSERT INTO clients (nome, cpf, email, data_cadastro, volume_total_investido, frequencia_movimentacoes, preferencia_investimento, perfil_risco, pontuacao_risco) VALUES
('João Silva', '123.456.789-00', 'joao.silva@email.com', CURRENT_TIMESTAMP, 50000.00, 5, 'EQUILIBRADO', 'MODERADO', 55),
('Maria Santos', '987.654.321-00', 'maria.santos@email.com', CURRENT_TIMESTAMP, 150000.00, 12, 'RENTABILIDADE', 'AGRESSIVO', 75),
('Pedro Oliveira', '456.789.123-00', 'pedro.oliveira@email.com', CURRENT_TIMESTAMP, 25000.00, 2, 'LIQUIDEZ', 'CONSERVADOR', 35),
('Ana Costa', '321.654.987-00', 'ana.costa@email.com', CURRENT_TIMESTAMP, 80000.00, 8, 'EQUILIBRADO', 'MODERADO', 60);

-- Inserir investimentos de exemplo
INSERT INTO investments (client_id, tipo, valor, rentabilidade, data, prazo_meses, status) VALUES
(1, 'CDB', 20000.00, 0.12, '2025-01-15', 12, 'ATIVO'),
(1, 'LCI', 15000.00, 0.10, '2025-03-10', 12, 'ATIVO'),
(1, 'TESOURO_DIRETO', 15000.00, 0.115, '2025-05-20', 24, 'ATIVO'),
(2, 'FUNDO_MULTIMERCADO', 50000.00, 0.18, '2025-01-05', 24, 'ATIVO'),
(2, 'FUNDO_ACOES', 30000.00, 0.20, '2025-02-10', 24, 'ATIVO'),
(2, 'CDB', 40000.00, 0.135, '2025-04-15', 18, 'ATIVO'),
(2, 'TESOURO_DIRETO', 30000.00, 0.125, '2025-06-01', 36, 'ATIVO'),
(3, 'POUPANCA', 10000.00, 0.07, '2025-02-01', 12, 'ATIVO'),
(3, 'LCA', 15000.00, 0.095, '2025-08-15', 12, 'ATIVO'),
(4, 'CDB', 25000.00, 0.12, '2025-01-20', 12, 'ATIVO'),
(4, 'FUNDO_RENDA_FIXA', 30000.00, 0.13, '2025-03-05', 18, 'ATIVO'),
(4, 'TESOURO_DIRETO', 25000.00, 0.115, '2025-05-10', 24, 'ATIVO');

-- Inserir algumas simulações de exemplo
INSERT INTO simulations (client_id, product_id, valor_investido, valor_final, rentabilidade_efetiva, prazo_meses, data_simulacao, imposto_renda, valor_liquido) VALUES
(1, 1, 10000.00, 11200.00, 0.12, 12, '2025-10-30 14:00:00', 45.00, 11155.00),
(1, 5, 5000.00, 5575.00, 0.115, 12, '2025-10-30 15:30:00', 28.75, 5546.25),
(2, 9, 20000.00, 23600.00, 0.18, 12, '2025-10-31 10:15:00', 810.00, 22790.00),
(3, 11, 3000.00, 3210.00, 0.07, 12, '2025-10-31 11:45:00', 47.25, 3162.75),
(4, 2, 15000.00, 17025.00, 0.135, 12, '2025-10-31 16:20:00', 455.62, 16569.38);
