# Painel de Investimentos

API REST desenvolvida em Java 21 com Spring Boot para análise de perfil de risco e simulação de investimentos.

## 📋 Descrição

Sistema que analisa o comportamento financeiro do cliente e ajusta automaticamente seu perfil de risco, sugerindo produtos de investimento como CDBs, LCIs, LCAs, Tesouro Direto, Fundos, etc.

### Funcionalidades Principais

- ✅ Simulação de investimentos com cálculo de rentabilidade e impostos
- ✅ Motor de recomendação baseado em volume, frequência e preferências
- ✅ Análise e classificação de perfil de risco (Conservador, Moderado, Agressivo)
- ✅ Histórico de simulações e investimentos
- ✅ Telemetria de serviços com volumes e tempos de resposta
- ✅ Autenticação JWT
- ✅ Documentação OpenAPI/Swagger
- ✅ Containerização com Docker
- ✅ Testes unitários

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security + JWT
- SQLite (pode usar SQL Server)
- Lombok
- OpenAPI/Swagger
- Docker & Docker Compose
- JUnit 5 + Mockito

## 📦 Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker & Docker Compose (opcional)

## 🔧 Instalação e Execução

### Opção 1: Executar localmente com Maven

```bash
# Clone o repositório
git clone <repository-url>
cd painel-investimentos

# Compile o projeto
mvn clean package

# Execute a aplicação
mvn spring-boot:run
```

### Opção 2: Executar com Docker

```bash
# Build e execução
docker-compose up --build

# Apenas execução (após build)
docker-compose up

# Parar containers
docker-compose down
```

A aplicação estará disponível em: `http://localhost:8080/api`

## 📚 Documentação da API

### Swagger UI

Acesse a documentação interativa em:
```
http://localhost:8080/api/swagger-ui.html
```

### Autenticação

**POST** `/api/auth/login`

```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

### Endpoints Principais

#### 1. Simular Investimento
**POST** `/api/v1/simular-investimento`

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "clienteId": 1,
  "valor": 10000.00,
  "prazoMeses": 12,
  "tipoProduto": "CDB"
}
```

**Response:**
```json
{
  "produtoValidado": {
    "id": 1,
    "nome": "CDB Caixa 2026",
    "tipo": "CDB",
    "rentabilidade": 0.12,
    "risco": "BAIXO"
  },
  "resultadoSimulacao": {
    "valorFinal": 11200.00,
    "rentabilidadeEfetiva": 0.12,
    "prazoMeses": 12,
    "impostoRenda": 45.00,
    "valorLiquido": 11155.00
  },
  "dataSimulacao": "2025-11-15T14:00:00"
}
```

#### 2. Histórico de Simulações
**GET** `/api/v1/simulacoes?clienteId=1`

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "id": 1,
    "clienteId": 1,
    "produto": "CDB Caixa 2026",
    "valorInvestido": 10000.00,
    "valorFinal": 11200.00,
    "prazoMeses": 12,
    "dataSimulacao": "2025-11-15T14:00:00"
  }
]
```

#### 3. Simulações por Produto e Dia
**GET** `/api/v1/simulacoes/por-produto-dia?dataInicio=2025-10-01&dataFim=2025-10-31`

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "produto": "CDB Caixa 2026",
    "data": "2025-10-30",
    "quantidadeSimulacoes": 15,
    "mediaValorFinal": 11050.00
  }
]
```

#### 4. Perfil de Risco
**GET** `/api/v1/perfil-risco/{clienteId}`

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "clienteId": 1,
  "perfil": "MODERADO",
  "pontuacao": 55,
  "descricao": "Perfil equilibrado entre segurança e rentabilidade."
}
```

#### 5. Produtos Recomendados
**GET** `/api/v1/produtos-recomendados/{perfil}`

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "id": 1,
    "nome": "CDB Caixa 2026",
    "tipo": "CDB",
    "rentabilidade": 0.12,
    "risco": "BAIXO",
    "prazoMinimoMeses": 6,
    "prazoMaximoMeses": 24,
    "valorMinimo": 1000.00,
    "valorMaximo": 1000000.00,
    "liquidezDias": 90,
    "descricao": "CDB com liquidez trimestral"
  }
]
```

#### 6. Histórico de Investimentos
**GET** `/api/v1/investimentos/{clienteId}`

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "id": 1,
    "tipo": "CDB",
    "valor": 5000.00,
    "rentabilidade": 0.12,
    "data": "2025-01-15",
    "prazoMeses": 12,
    "status": "ATIVO"
  }
]
```

#### 7. Telemetria
**GET** `/api/v1/telemetria?inicio=2025-10-01&fim=2025-10-31`

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "servicos": [
    {
      "nome": "simular-investimento",
      "quantidadeChamadas": 120,
      "mediaTempoRespostaMs": 250
    },
    {
      "nome": "perfil-risco",
      "quantidadeChamadas": 80,
      "mediaTempoRespostaMs": 180
    }
  ],
  "periodo": {
    "inicio": "2025-10-01",
    "fim": "2025-10-31"
  }
}
```

## 🎯 Motor de Recomendação

O sistema utiliza um algoritmo de pontuação baseado em três critérios:

### 1. Volume de Investimentos (Peso: 40%)
- Até R$ 10.000: 10 pontos
- R$ 10.001 a R$ 50.000: 20 pontos
- R$ 50.001 a R$ 100.000: 30 pontos
- Acima de R$ 100.000: 40 pontos

### 2. Frequência de Movimentações (Peso: 30%)
- 0-2 movimentações/ano: 5 pontos (conservador)
- 3-6 movimentações/ano: 15 pontos
- 7-12 movimentações/ano: 25 pontos
- Mais de 12/ano: 30 pontos (agressivo)

### 3. Preferência de Investimento (Peso: 30%)
- Produtos conservadores (CDB, LCI, LCA, Poupança): 10 pontos
- Produtos moderados (Tesouro, Fundo Renda Fixa): 20 pontos
- Produtos agressivos (Fundos, Multimercado, Ações): 30 pontos

### Classificação Final
- **Conservador**: 0-40 pontos (foco em segurança e liquidez)
- **Moderado**: 41-70 pontos (equilíbrio entre segurança e rentabilidade)
- **Agressivo**: 71-100 pontos (busca alta rentabilidade)

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar com cobertura
mvn test jacoco:report
```

## 🔐 Segurança

- Autenticação via JWT (JSON Web Token)
- Senhas criptografadas com BCrypt
- Endpoints protegidos (exceto login e documentação)
- Tokens com expiração de 24 horas

### Usuários Padrão

| Username | Password | Role |
|----------|----------|------|
| admin | password123 | ADMIN |
| user | password123 | USER |

## 📊 Banco de Dados

O projeto usa SQLite por padrão, mas pode ser facilmente configurado para SQL Server:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=investimentos
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
    username: sa
    password: your_password
```

## 🐳 Docker

### Dockerfile
- Multi-stage build para otimização
- Imagem base: eclipse-temurin:21
- Expõe porta 8080

### docker-compose.yml
- Health check configurado
- Volume para persistência de dados
- Rede isolada

## 📁 Estrutura do Projeto

```
painel-investimentos/
├── src/
│   ├── main/
│   │   ├── java/com/caixa/invest/
│   │   │   ├── config/          # Configurações (OpenAPI, etc)
│   │   │   ├── controller/      # Controllers REST
│   │   │   ├── domain/          # Entidades JPA
│   │   │   ├── dto/             # Request/Response DTOs
│   │   │   ├── exception/       # Exception handlers
│   │   │   ├── repository/      # Repositories JPA
│   │   │   ├── security/        # JWT e Security config
│   │   │   └── service/         # Lógica de negócio
│   │   └── resources/
│   │       ├── application.yml  # Configurações
│   │       └── data.sql         # Dados iniciais
│   └── test/                    # Testes unitários
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 🚦 Status dos Requisitos

- [x] API em Java 21
- [x] Envelope JSON de entrada/saída
- [x] Banco de dados SQLite
- [x] Validação de dados
- [x] Filtro de produtos adequados
- [x] Cálculos de simulação
- [x] Persistência de simulações
- [x] Endpoint histórico de simulações
- [x] Endpoint simulações por produto/dia
- [x] Endpoint telemetria
- [x] Docker/Docker Compose
- [x] Autenticação JWT
- [x] Motor de Recomendação
- [x] Perfil de risco dinâmico
- [x] Testes unitários

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👥 Contato

Para dúvidas ou sugestões, entre em contato através de contato@caixa.com
