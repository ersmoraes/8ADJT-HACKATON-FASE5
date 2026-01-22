# SUS Agenda - Sistema de Agendamento de Consultas para o SUS

## 📋 Sobre o Projeto

O **SUS Agenda** é um MVP (Produto Mínimo Viável) desenvolvido para o **Hackathon FIAP - Pós-graduação em Arquitetura e Desenvolvimento Java**. O sistema visa otimizar o processo de agendamento de consultas e exames no Sistema Único de Saúde (SUS), promovendo maior eficiência, agilidade e qualidade nos serviços de saúde pública.

### 🎯 Problema Abordado

O agendamento de consultas e exames no SUS frequentemente enfrenta desafios como:
- Longas filas de espera
- Dificuldade para encontrar horários disponíveis
- Falta de transparência na posição da fila
- Ausência de sistema de priorização adequado
- Gestão ineficiente dos horários dos profissionais

### 💡 Solução Proposta

O SUS Agenda oferece:
- **Agendamento simplificado** de consultas e exames
- **Busca de vagas disponíveis** por especialidade e período
- **Lista de espera inteligente** com priorização automática
- **Gestão de horários** dos profissionais de saúde
- **Fluxo completo de atendimento** (agendado → confirmado → em atendimento → concluído)
- **API REST documentada** para integração com outros sistemas

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
┌─────────────────────────────────────────────────────────────────┐
│                    Controllers (REST API)                        │
├─────────────────────────────────────────────────────────────────┤
│                    Services (Regras de Negócio)                 │
├─────────────────────────────────────────────────────────────────┤
│                    Repositories (JPA/Hibernate)                 │
├─────────────────────────────────────────────────────────────────┤
│                    PostgreSQL Database                           │
└─────────────────────────────────────────────────────────────────┘
```

### Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 17 | Linguagem de programação |
| Spring Boot | 3.2.0 | Framework principal |
| Spring Data JPA | 3.2.0 | Persistência de dados |
| PostgreSQL | 15+ | Banco de dados relacional |
| Flyway | 9.22.3 | Migrations de banco de dados |
| Lombok | 1.18.30 | Redução de código boilerplate |
| MapStruct | 1.5.5 | Mapeamento de DTOs |
| SpringDoc OpenAPI | 2.3.0 | Documentação da API (Swagger) |
| JUnit 5 | 5.10.0 | Framework de testes |
| Mockito | 5.7.0 | Mock de dependências |

---

## 📁 Estrutura do Projeto

```
sus-agenda/
├── src/
│   ├── main/
│   │   ├── java/br/com/susagenda/
│   │   │   ├── config/           # Configurações (OpenAPI)
│   │   │   ├── controller/       # Controllers REST
│   │   │   ├── domain/
│   │   │   │   ├── entity/       # Entidades JPA
│   │   │   │   └── enums/        # Enumerações
│   │   │   ├── dto/
│   │   │   │   ├── request/      # DTOs de entrada
│   │   │   │   └── response/     # DTOs de saída
│   │   │   ├── exception/        # Tratamento de exceções
│   │   │   ├── repository/       # Repositórios JPA
│   │   │   └── service/          # Serviços de negócio
│   │   └── resources/
│   │       ├── db/migration/     # Scripts Flyway
│   │       ├── application.yml   # Configurações da aplicação
│   │       └── application-test.yml
│   └── test/                     # Testes unitários e integração
├── docker-compose.yml            # Configuração Docker
├── pom.xml                       # Dependências Maven
└── README.md
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose (para o banco de dados)

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/sus-agenda.git
cd sus-agenda
```

### 2. Inicie o banco de dados com Docker

```bash
docker-compose up -d
```

### 3. Execute a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### 4. Acesse a documentação da API

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

---

## 📚 Endpoints da API

### Pacientes
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/pacientes` | Cadastrar paciente |
| GET | `/api/v1/pacientes` | Listar pacientes |
| GET | `/api/v1/pacientes/{id}` | Buscar por ID |
| GET | `/api/v1/pacientes/cpf/{cpf}` | Buscar por CPF |
| GET | `/api/v1/pacientes/cartao-sus/{cartaoSus}` | Buscar por Cartão SUS |
| PUT | `/api/v1/pacientes/{id}` | Atualizar paciente |
| PATCH | `/api/v1/pacientes/{id}/desativar` | Desativar paciente |

### Unidades de Saúde
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/unidades-saude` | Cadastrar unidade |
| GET | `/api/v1/unidades-saude` | Listar unidades |
| GET | `/api/v1/unidades-saude/{id}` | Buscar por ID |
| GET | `/api/v1/unidades-saude/cnes/{cnes}` | Buscar por CNES |
| GET | `/api/v1/unidades-saude/cidade/{cidade}` | Buscar por cidade |

### Profissionais
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/profissionais` | Cadastrar profissional |
| GET | `/api/v1/profissionais` | Listar profissionais |
| GET | `/api/v1/profissionais/especialidade/{especialidade}` | Buscar por especialidade |
| GET | `/api/v1/profissionais/especialidades` | Listar especialidades disponíveis |

### Agendamentos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/agendamentos` | Criar agendamento |
| GET | `/api/v1/agendamentos/{id}` | Buscar por ID |
| GET | `/api/v1/agendamentos/paciente/{pacienteId}` | Buscar por paciente |
| GET | `/api/v1/agendamentos/vagas-disponiveis` | Buscar vagas disponíveis |
| PATCH | `/api/v1/agendamentos/{id}/confirmar` | Confirmar agendamento |
| PATCH | `/api/v1/agendamentos/{id}/registrar-chegada` | Registrar chegada |
| PATCH | `/api/v1/agendamentos/{id}/iniciar-atendimento` | Iniciar atendimento |
| PATCH | `/api/v1/agendamentos/{id}/concluir` | Concluir atendimento |
| PATCH | `/api/v1/agendamentos/{id}/cancelar` | Cancelar agendamento |

### Lista de Espera
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/lista-espera` | Adicionar à fila |
| GET | `/api/v1/lista-espera/especialidade/{especialidade}` | Buscar fila por especialidade |
| GET | `/api/v1/lista-espera/paciente/{pacienteId}` | Buscar por paciente |
| PATCH | `/api/v1/lista-espera/{id}/atendido` | Marcar como atendido |

---

## 🎯 Funcionalidades Principais

### 1. Sistema de Priorização

O sistema implementa priorização automática baseada em:

| Prioridade | Peso | Descrição |
|------------|------|-----------|
| URGENTE | 3 | Casos de urgência |
| GESTANTE | 2 | Gestantes |
| DEFICIENTE | 2 | Pessoas com deficiência |
| IDOSO | 1 | Pessoas com 60+ anos |
| CRIANCA | 1 | Crianças (0-12 anos) |
| NORMAL | 0 | Prioridade padrão |

### 2. Fluxo de Atendimento

```
AGENDADO → CONFIRMADO → CHEGADA REGISTRADA → EM_ATENDIMENTO → CONCLUIDO
                ↓                                    ↓
          CANCELADO                           NAO_COMPARECEU
```

### 3. Busca de Vagas Disponíveis

Permite buscar vagas por:
- Especialidade médica
- Período (data inicial e final)
- Retorna horários disponíveis com informações do profissional e unidade

---

## 🧪 Testes

### Executar testes

```bash
mvn test
```

### Cobertura de testes

O projeto inclui testes unitários para:
- Services (PacienteService, AgendamentoService)
- Controllers (PacienteController)

---

## 📊 Modelo de Dados

### Entidades Principais

- **Paciente**: Dados do paciente (CPF, Cartão SUS, prioridade)
- **UnidadeSaude**: UBS, hospitais, clínicas (CNES)
- **Profissional**: Médicos e profissionais de saúde (CRM)
- **HorarioDisponivel**: Configuração de horários de atendimento
- **Agendamento**: Consultas e exames agendados
- **ListaEspera**: Fila de espera por especialidade

---

## 🔒 Validações

O sistema implementa validações robustas:
- CPF: 11 dígitos numéricos
- Cartão SUS: 15 dígitos numéricos
- CNES: 7 dígitos numéricos
- E-mail: Formato válido
- Datas: Validação de passado/futuro
- Campos obrigatórios: Validação automática

---

## 📈 Impacto Esperado

- **Redução de filas**: Agendamento prévio e distribuição de horários
- **Transparência**: Paciente acompanha posição na fila de espera
- **Eficiência**: Gestão otimizada dos horários dos profissionais
- **Priorização justa**: Atendimento prioritário para grupos vulneráveis
- **Integração**: API REST para integração com outros sistemas

---

## 🔮 Próximos Passos (Roadmap)

1. **Autenticação e Autorização** (Spring Security + JWT)
2. **Notificações** (SMS/E-mail para lembrete de consultas)
3. **Relatórios e Dashboards** (Métricas de atendimento)
4. **Integração com sistemas governamentais** (CNES, CNS)
5. **App mobile** para pacientes
6. **Cache distribuído** (Redis) para melhor performance
7. **Mensageria** (RabbitMQ/Kafka) para processamento assíncrono

---

## 👥 Equipe

Desenvolvido para o Hackathon FIAP - Pós-graduação em Arquitetura e Desenvolvimento Java

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
