# Painel de Investimentos

API REST desenvolvida em Java 21 com **Quarkus 3.8.6** para análise de perfil de risco e simulação de investimentos.

> 🚀 **Migrado de Spring Boot para Quarkus** - Aplicação modernizada com startup ultra-rápido e menor consumo de memória.

## 📋 Descrição

Sistema que analisa o comportamento financeiro do cliente e ajusta automaticamente seu perfil de risco, sugerindo produtos de investimento como CDBs, LCIs, LCAs, Tesouro Direto, Fundos, etc.

### Funcionalidades Principais

- ✅ Simulação de investimentos com cálculo de rentabilidade e impostos
- ✅ Motor de recomendação baseado em volume, frequência e preferências
- ✅ Análise e classificação de perfil de risco (Conservador, Moderado, Agressivo)
- ✅ Histórico de simulações e investimentos
- ✅ Telemetria de serviços com volumes e tempos de resposta
- ✅ Autenticação JWT (RS256 com SmallRye JWT)
- ✅ Documentação OpenAPI/Swagger
- ✅ Containerização com Docker
- ✅ Testes unitários e integração (34 testes, 100% passando)
- ✅ Cobertura de código (JaCoCo)

## 🚀 Tecnologias

- **Java 21** (Microsoft JDK)
- **Quarkus 3.8.6 LTS** (Supersonic Subatomic Java)
- **Hibernate ORM with Panache** (Active Record pattern)
- **RESTEasy Reactive** (Non-blocking REST)
- **SmallRye JWT** (MicroProfile JWT RBAC)
- **H2 Database** (in-memory para testes)
- **Lombok** (Builders e getters/setters)
- **SmallRye OpenAPI** (Swagger UI)
- **Docker & Docker Compose**
- **JUnit 5** + **Mockito** + **RestAssured**
- **JaCoCo** (Code Coverage)
- **Maven 3.9.6**

## 📦 Pré-requisitos

- Java 21+ (Microsoft JDK ou OpenJDK)
- Maven 3.9+
- Docker & Docker Compose (opcional)

## 🔧 Instalação e Execução

### Opção 1: Executar localmente com Maven

```bash
# Clone o repositório
git clone https://github.com/alfredosantos83/Painel-de-Investimentos.git
cd painel-investimentos

# Compile o projeto
mvn clean package

# Execute a aplicação Quarkus
mvn quarkus:dev
```

**Modo de desenvolvimento** (`quarkus:dev`):
- Live reload automático
- Dev UI: http://localhost:8081/q/dev
- Swagger UI: http://localhost:8081/q/swagger-ui

### Opção 2: Executar com Docker

```bash
# Build e execução
docker-compose up --build

# Apenas execução (após build)
docker-compose up

# Parar containers
docker-compose down
```

A aplicação estará disponível em: `http://localhost:8081`

**Endpoints principais:**
- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/q/swagger-ui`
- Health Check: `http://localhost:8081/q/health`
- Metrics: `http://localhost:8081/q/metrics`

## 📚 Documentação da API

### Swagger UI (OpenAPI 3.0)

Acesse a documentação interativa em:
```
http://localhost:8081/swagger-ui
```

> ⚠️ **Problema conhecido:** O botão "Authorize" do Swagger UI não funciona corretamente no Quarkus 3.8.6. Use **Postman** ou **test-api.ps1** para testar a API.

### 🧪 Testando com Postman (Recomendado)

#### 1. Importar Collection

Importe o arquivo `Painel-Investimentos.postman_collection.json` no Postman:

1. Abra o **Postman**
2. Clique em **Import** (canto superior esquerdo)
3. Selecione o arquivo `Painel-Investimentos.postman_collection.json`
4. Clique **Import**

A collection inclui:
- ✅ 7 requests pré-configuradas
- ✅ Variáveis automáticas (`base_url`, `jwt_token`)
- ✅ Scripts de automação (token salvo automaticamente)
- ✅ Autenticação Bearer configurada

#### 2. Fazer Login

Execute a request **"Login Admin"** ou **"Login User"**:

**Endpoint:** `POST http://localhost:8081/auth/login`

**Body (JSON):**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJ0eXAiOiJKV1QiLCJhbGc...",
  "type": "Bearer",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

> 💡 O token JWT é **automaticamente salvo** na variável `{{jwt_token}}` quando você usa a collection.

#### 3. Testar Endpoints Protegidos

Após o login, teste os endpoints da pasta **"Secured Endpoints"**:

**Get Profile** - `GET /secure/profile`
```json
{
  "username": "admin",
  "email": "admin@caixa.com",
  "roles": ["ADMIN"]
}
```

**Admin Area** - `GET /secure/admin` (somente ADMIN)
```json
{
  "message": "Bem-vindo, administrador!",
  "user": "admin",
  "access": "ADMIN"
}
```

**User Area** - `GET /secure/user` (USER ou ADMIN)
```json
{
  "message": "Área do usuário",
  "user": "admin",
  "access": "USER"
}
```

#### 4. Credenciais Disponíveis

| Usuário | Senha | Role |
|---------|-------|------|
| `admin` | `password123` | ADMIN |
| `user` | `password123` | USER |

#### 5. Configuração Manual (sem collection)

Se preferir configurar manualmente:

1. **Faça login** e copie o token da resposta
2. Na aba **"Authorization"**:
   - Type: `Bearer Token`
   - Token: cole o token (sem "Bearer", sem aspas)
3. Envie a request

### 🧪 Testando com PowerShell

Execute o script de testes automatizado:

```powershell
.\test-api.ps1
```

**Resultado:**
```
🧪 Executando Suite de Testes da API...
1️⃣ Health Check ✅ Status: UP
2️⃣ Login Admin ✅ Token obtido
3️⃣ Login User ✅ Token obtido
4️⃣ Perfil Admin ✅ Username: admin
5️⃣ Área Admin (Admin) ✅ Acesso permitido
6️⃣ Área User (Admin) ✅ Acesso permitido
7️⃣ Área User (User) ✅ Acesso permitido
8️⃣ Segurança: User → Admin ✅ Bloqueado (403)
9️⃣ Segurança: Sem token ✅ Bloqueado (401)
🔟 Segurança: Token inválido ✅ Bloqueado (401)
✨ Todos os testes executados com sucesso!
```

### Autenticação JWT

**POST** `/auth/login`

```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJ0eXAiOiJKV1QiLCJhbGc...",
  "type": "Bearer",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

### Endpoints Principais

> **Nota:** Os endpoints não usam prefixo `/api`. Acesse diretamente pela raiz.

#### 1. Simular Investimento
**POST** `/v1/simular-investimento`

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
**GET** `/v1/simulacoes?clienteId=1`

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
**GET** `/v1/simulacoes/por-produto-dia?dataInicio=2025-10-01&dataFim=2025-10-31`

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
**GET** `/v1/perfil-risco/{clienteId}`

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
**GET** `/v1/produtos-recomendados/{perfil}`

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
**GET** `/v1/investimentos/{clienteId}`

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
**GET** `/v1/telemetria?inicio=2025-10-01&fim=2025-10-31`

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
mvn clean test jacoco:report

# Ver relatório de cobertura
# Abrir: target/site/jacoco/index.html
```

**Status dos Testes:**
- ✅ 34/34 testes passando (100%)
- ✅ AuthControllerTest: 4 testes
- ✅ SecureControllerTest: 11 testes (autenticação JWT completa)
- ✅ DebugControllerTest: 2 testes
- ✅ HealthTestControllerTest: 2 testes
- ✅ AuthServiceTest: 5 testes
- ✅ JwtTokenProviderTest: 3 testes
- ✅ UserTest: 4 testes
- ✅ ClientTest: 3 testes
- 📊 Cobertura: 20% (controllers: 40%)

## 🔐 Segurança

- Autenticação via **JWT RS256** (MicroProfile JWT)
- Chaves públicas/privadas RSA para assinatura de tokens
- Senhas criptografadas com **BCrypt**
- Endpoints protegidos com `@RolesAllowed`
- Tokens com expiração de 24 horas
- Validação de roles (USER, ADMIN)

### Usuários Padrão

| Username | Password | Role |
|----------|----------|------|
| admin | password123 | ADMIN |
| user | password123 | USER |

## 📊 Banco de Dados

O projeto usa **H2 Database** (in-memory) para desenvolvimento e testes:

```yaml
quarkus:
  datasource:
    db-kind: h2
    jdbc:
      url: jdbc:h2:mem:investimentos;DB_CLOSE_DELAY=-1
      
  hibernate-orm:
    database:
      generation: drop-and-create
    sql-load-script: data.sql
```

Para produção, pode ser configurado para PostgreSQL, MySQL ou SQL Server:

```yaml
quarkus:
  datasource:
    db-kind: postgresql
    jdbc:
      url: jdbc:postgresql://localhost:5432/investimentos
    username: postgres
    password: your_password
```

## 🐳 Docker

### Dockerfile
- Multi-stage build com Quarkus
- Imagem base: `registry.access.redhat.com/ubi9/openjdk-21`
- Modo JVM otimizado
- Expõe porta 8081
- Health check configurado

### docker-compose.yml
- Container `painel-investimentos-quarkus`
- Health check via `/health-test`
- Restart automático
- Porta 8081:8081

## 📁 Estrutura do Projeto

```
painel-investimentos/
├── src/
│   ├── main/
│   │   ├── java/com/caixa/invest/
│   │   │   ├── config/          # OpenApiConfig (Swagger)
│   │   │   ├── controller/      # REST Controllers (@Path)
│   │   │   ├── domain/          # Entidades Panache (Active Record)
│   │   │   ├── dto/             # Request/Response DTOs
│   │   │   ├── exception/       # Exception handlers
│   │   │   ├── repository/      # Repositories Panache
│   │   │   ├── security/        # JWT Provider e Security Config
│   │   │   └── service/         # Lógica de negócio
│   │   └── resources/
│   │       ├── application.yml  # Configurações Quarkus
│   │       ├── data.sql         # Dados iniciais
│   │       └── META-INF/
│   │           └── resources/
│   │               ├── publicKey.pem   # Chave pública JWT
│   │               └── privateKey.pem  # Chave privada JWT
│   └── test/                    # Testes (34 testes)
│       └── java/com/caixa/invest/
│           ├── controller/      # Testes REST (AuthController, SecureController)
│           ├── domain/          # Testes de entidades
│           ├── security/        # Testes JWT
│           └── service/         # Testes de serviços
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 🚦 Status dos Requisitos

- [x] API em Java 21 com Quarkus 3.8.6
- [x] Envelope JSON de entrada/saída
- [x] Banco de dados H2 (in-memory)
- [x] Validação de dados
- [x] Filtro de produtos adequados
- [x] Cálculos de simulação
- [x] Persistência de simulações
- [x] Endpoint histórico de simulações
- [x] Endpoint simulações por produto/dia
- [x] Endpoint telemetria
- [x] Docker/Docker Compose
- [x] Autenticação JWT (RS256 com SmallRye JWT)
- [x] Motor de Recomendação
- [x] Perfil de risco dinâmico
- [x] Testes unitários e integração (34/34 passando)
- [x] Análise de código com JaCoCo (20% cobertura)
- [x] Migração completa Spring Boot → Quarkus

## ⚡ Vantagens do Quarkus

### Performance
- 🚀 **Startup ultra-rápido**: ~2-3 segundos (vs ~8-10s Spring Boot)
- 💾 **Menor consumo de memória**: ~30-50% menos RAM
- ⚡ **Resposta mais rápida**: Processamento reativo com RESTEasy

### Developer Experience
- 🔥 **Live Reload**: Alterações refletem instantaneamente
- 🛠️ **Dev UI**: Interface web para gerenciar aplicação (`/q/dev`)
- 📊 **Métricas embutidas**: Health, metrics prontos out-of-the-box

### Cloud Native
- ☁️ **Otimizado para containers**: Menor tamanho de imagem
- 🎯 **Kubernetes-ready**: Suporte nativo a K8s
- 📦 **GraalVM native**: Pode compilar para binário nativo (opcional)

## 🔍 Qualidade de Código

### JaCoCo Code Coverage

Execute os testes com cobertura:

```bash
# Gerar relatório de cobertura
mvn clean test jacoco:report

# Visualizar relatório
# Abrir em navegador: target/site/jacoco/index.html
```

**Métricas atuais:**
- Cobertura total: 20%
- Controllers: 40% ✅
- Services: 0%
- Domain: 0%
- Security: 18%

### SonarQube Local

Execute análise local com SonarQube:

```bash
# Iniciar SonarQube via Docker
docker run -d --name sonarqube -p 9000:9000 sonarqube:community

# Executar análise
mvn clean verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=YOUR_TOKEN
```

### Executar análise localmente

```bash
# Executar testes com cobertura
mvn clean verify

# Executar análise do SonarQube (requer token)
mvn sonar:sonar -Dsonar.token=YOUR_SONAR_TOKEN
```

## 📖 Documentação Adicional

- [README_QUARKUS.md](README_QUARKUS.md) - Guia completo da migração Spring Boot → Quarkus
- [Quarkus Documentation](https://quarkus.io/guides/) - Documentação oficial
- [SmallRye JWT](https://smallrye.io/smallrye-jwt/) - JWT RBAC implementation

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👨‍💻 Autor

**Alfredo Santos**
- GitHub: [@alfredosantos83](https://github.com/alfredosantos83)
- LinkedIn: [Alfredo Santos](https://linkedin.com/in/alfredosantos83)

## 🙏 Agradecimentos

- Projeto migrado com sucesso de **Spring Boot 3.5.0** para **Quarkus 3.8.6**
- Todos os testes mantidos e funcionando (34/34 ✅)
- Autenticação JWT RS256 implementada com SmallRye
- Performance e consumo de memória otimizados

---

⭐ Se este projeto foi útil, considere dar uma estrela no GitHub!
