-- SUS Agenda - Sistema de Agendamento de Consultas
-- Migration V1: Criação das tabelas iniciais

-- Tabela de Unidades de Saúde
CREATE TABLE unidades_saude (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cnes VARCHAR(20) NOT NULL UNIQUE,
    endereco VARCHAR(300) NOT NULL,
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(10),
    telefone VARCHAR(20),
    email VARCHAR(100),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Tabela de Pacientes
CREATE TABLE pacientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    cartao_sus VARCHAR(15) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(300),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    prioridade VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Tabela de Profissionais
CREATE TABLE profissionais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    registro_profissional VARCHAR(20) NOT NULL UNIQUE,
    especialidade VARCHAR(50) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100),
    unidade_saude_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_profissional_unidade FOREIGN KEY (unidade_saude_id) REFERENCES unidades_saude(id)
);

-- Tabela de Horários Disponíveis
CREATE TABLE horarios_disponiveis (
    id BIGSERIAL PRIMARY KEY,
    profissional_id BIGINT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    duracao_consulta_minutos INTEGER NOT NULL DEFAULT 30,
    vagas_por_horario INTEGER NOT NULL DEFAULT 1,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_horario_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais(id)
);

-- Tabela de Agendamentos
CREATE TABLE agendamentos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    unidade_saude_id BIGINT NOT NULL,
    data_agendamento DATE NOT NULL,
    hora_agendamento TIME NOT NULL,
    tipo_atendimento VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'AGENDADO',
    observacoes VARCHAR(500),
    motivo_cancelamento VARCHAR(300),
    data_confirmacao TIMESTAMP,
    data_chegada TIMESTAMP,
    data_inicio_atendimento TIMESTAMP,
    data_fim_atendimento TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_agendamento_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_agendamento_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais(id),
    CONSTRAINT fk_agendamento_unidade FOREIGN KEY (unidade_saude_id) REFERENCES unidades_saude(id)
);

-- Tabela de Lista de Espera
CREATE TABLE lista_espera (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    especialidade VARCHAR(50) NOT NULL,
    unidade_saude_id BIGINT,
    prioridade VARCHAR(20) NOT NULL,
    data_solicitacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_agendamento TIMESTAMP,
    atendido BOOLEAN NOT NULL DEFAULT FALSE,
    observacoes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_lista_espera_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_lista_espera_unidade FOREIGN KEY (unidade_saude_id) REFERENCES unidades_saude(id)
);

-- Índices para otimização de consultas
CREATE INDEX idx_pacientes_cpf ON pacientes(cpf);
CREATE INDEX idx_pacientes_cartao_sus ON pacientes(cartao_sus);
CREATE INDEX idx_pacientes_nome ON pacientes(nome);

CREATE INDEX idx_profissionais_especialidade ON profissionais(especialidade);
CREATE INDEX idx_profissionais_unidade ON profissionais(unidade_saude_id);

CREATE INDEX idx_agendamentos_data ON agendamentos(data_agendamento);
CREATE INDEX idx_agendamentos_paciente ON agendamentos(paciente_id);
CREATE INDEX idx_agendamentos_profissional ON agendamentos(profissional_id);
CREATE INDEX idx_agendamentos_status ON agendamentos(status);
CREATE INDEX idx_agendamentos_profissional_data ON agendamentos(profissional_id, data_agendamento);

CREATE INDEX idx_lista_espera_especialidade ON lista_espera(especialidade);
CREATE INDEX idx_lista_espera_paciente ON lista_espera(paciente_id);
CREATE INDEX idx_lista_espera_prioridade ON lista_espera(prioridade, data_solicitacao);

CREATE INDEX idx_horarios_profissional ON horarios_disponiveis(profissional_id);
CREATE INDEX idx_horarios_dia_semana ON horarios_disponiveis(profissional_id, dia_semana);
