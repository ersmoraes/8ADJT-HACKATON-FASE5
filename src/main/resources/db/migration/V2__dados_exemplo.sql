-- SUS Agenda - Sistema de Agendamento de Consultas
-- Migration V2: Dados de exemplo para demonstração do MVP

-- Unidades de Saúde
INSERT INTO unidades_saude (nome, cnes, endereco, bairro, cidade, estado, cep, telefone, email) VALUES
('UBS Centro', '1234567', 'Rua das Flores, 100', 'Centro', 'São Paulo', 'SP', '01310100', '1133334444', 'ubscentro@saude.sp.gov.br'),
('UBS Vila Nova', '2345678', 'Av. Brasil, 500', 'Vila Nova', 'São Paulo', 'SP', '04538132', '1155556666', 'ubsvilanova@saude.sp.gov.br'),
('Hospital Municipal', '3456789', 'Rua da Saúde, 1000', 'Jardim Paulista', 'São Paulo', 'SP', '01310200', '1199998888', 'hospital@saude.sp.gov.br');

-- Pacientes
INSERT INTO pacientes (nome, cpf, cartao_sus, data_nascimento, telefone, email, endereco, bairro, cidade, estado, cep, prioridade) VALUES
('Maria Silva Santos', '12345678901', '123456789012345', '1985-05-15', '11987654321', 'maria.santos@email.com', 'Rua das Palmeiras, 50', 'Centro', 'São Paulo', 'SP', '01310100', 'NORMAL'),
('João Pedro Oliveira', '23456789012', '234567890123456', '1960-03-20', '11976543210', 'joao.oliveira@email.com', 'Av. Paulista, 1500', 'Bela Vista', 'São Paulo', 'SP', '01310300', 'IDOSO'),
('Ana Clara Costa', '34567890123', '345678901234567', '1995-08-10', '11965432109', 'ana.costa@email.com', 'Rua Augusta, 200', 'Consolação', 'São Paulo', 'SP', '01305000', 'GESTANTE'),
('Carlos Eduardo Lima', '45678901234', '456789012345678', '2015-12-25', '11954321098', 'carlos.lima@email.com', 'Rua Oscar Freire, 300', 'Jardins', 'São Paulo', 'SP', '01426001', 'CRIANCA'),
('Fernanda Rodrigues', '56789012345', '567890123456789', '1978-07-08', '11943210987', 'fernanda.r@email.com', 'Alameda Santos, 400', 'Cerqueira César', 'São Paulo', 'SP', '01419001', 'NORMAL');

-- Profissionais
INSERT INTO profissionais (nome, cpf, registro_profissional, especialidade, telefone, email, unidade_saude_id) VALUES
('Dr. Roberto Almeida', '98765432101', 'CRM-SP-123456', 'CLINICO_GERAL', '11912345678', 'dr.roberto@saude.sp.gov.br', 1),
('Dra. Patrícia Souza', '87654321012', 'CRM-SP-234567', 'PEDIATRIA', '11923456789', 'dra.patricia@saude.sp.gov.br', 1),
('Dr. Marcos Ferreira', '76543210123', 'CRM-SP-345678', 'CARDIOLOGIA', '11934567890', 'dr.marcos@saude.sp.gov.br', 2),
('Dra. Juliana Mendes', '65432101234', 'CRM-SP-456789', 'GINECOLOGIA', '11945678901', 'dra.juliana@saude.sp.gov.br', 2),
('Dr. André Santos', '54321012345', 'CRM-SP-567890', 'ORTOPEDIA', '11956789012', 'dr.andre@saude.sp.gov.br', 3);

-- Horários Disponíveis
INSERT INTO horarios_disponiveis (profissional_id, dia_semana, hora_inicio, hora_fim, duracao_consulta_minutos, vagas_por_horario) VALUES
-- Dr. Roberto - Clínico Geral (Segunda a Sexta)
(1, 'MONDAY', '08:00', '12:00', 30, 1),
(1, 'MONDAY', '14:00', '18:00', 30, 1),
(1, 'TUESDAY', '08:00', '12:00', 30, 1),
(1, 'TUESDAY', '14:00', '18:00', 30, 1),
(1, 'WEDNESDAY', '08:00', '12:00', 30, 1),
(1, 'THURSDAY', '08:00', '12:00', 30, 1),
(1, 'THURSDAY', '14:00', '18:00', 30, 1),
(1, 'FRIDAY', '08:00', '12:00', 30, 1),

-- Dra. Patrícia - Pediatria (Segunda, Quarta e Sexta)
(2, 'MONDAY', '08:00', '12:00', 20, 1),
(2, 'WEDNESDAY', '08:00', '12:00', 20, 1),
(2, 'WEDNESDAY', '14:00', '17:00', 20, 1),
(2, 'FRIDAY', '08:00', '12:00', 20, 1),

-- Dr. Marcos - Cardiologia (Terça e Quinta)
(3, 'TUESDAY', '09:00', '13:00', 40, 1),
(3, 'THURSDAY', '09:00', '13:00', 40, 1),

-- Dra. Juliana - Ginecologia (Segunda a Quinta)
(4, 'MONDAY', '14:00', '18:00', 30, 1),
(4, 'TUESDAY', '14:00', '18:00', 30, 1),
(4, 'WEDNESDAY', '14:00', '18:00', 30, 1),
(4, 'THURSDAY', '14:00', '18:00', 30, 1),

-- Dr. André - Ortopedia (Segunda, Quarta e Sexta)
(5, 'MONDAY', '07:00', '12:00', 30, 2),
(5, 'WEDNESDAY', '07:00', '12:00', 30, 2),
(5, 'FRIDAY', '07:00', '12:00', 30, 2);

-- Alguns agendamentos de exemplo
INSERT INTO agendamentos (paciente_id, profissional_id, unidade_saude_id, data_agendamento, hora_agendamento, tipo_atendimento, status, observacoes) VALUES
(1, 1, 1, CURRENT_DATE + INTERVAL '1 day', '09:00', 'CONSULTA', 'AGENDADO', 'Primeira consulta - dores de cabeça frequentes'),
(2, 3, 2, CURRENT_DATE + INTERVAL '2 days', '10:00', 'CONSULTA', 'CONFIRMADO', 'Acompanhamento cardiológico'),
(4, 2, 1, CURRENT_DATE + INTERVAL '1 day', '08:00', 'CONSULTA', 'AGENDADO', 'Consulta de rotina - puericultura'),
(3, 4, 2, CURRENT_DATE + INTERVAL '3 days', '15:00', 'CONSULTA', 'AGENDADO', 'Acompanhamento pré-natal'),
(5, 5, 3, CURRENT_DATE + INTERVAL '4 days', '08:00', 'CONSULTA', 'AGENDADO', 'Dor no joelho esquerdo');

-- Lista de Espera
INSERT INTO lista_espera (paciente_id, especialidade, unidade_saude_id, prioridade, observacoes) VALUES
(1, 'DERMATOLOGIA', 1, 'NORMAL', 'Manchas na pele - aguardando vaga'),
(2, 'OFTALMOLOGIA', 2, 'IDOSO', 'Dificuldade para enxergar de longe'),
(5, 'NEUROLOGIA', NULL, 'NORMAL', 'Dores de cabeça persistentes - aceita qualquer unidade');
