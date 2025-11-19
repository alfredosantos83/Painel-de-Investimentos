# Painel de Investimentos - Quarkus

API de análise de perfil de risco e simulação de investimentos migrada para **Quarkus 3.8.6 LTS**.

## 🚀 Tecnologias

- **Java 21**
- **Quarkus 3.8.6 LTS** (anteriormente Spring Boot 3.5.0)
- **Hibernate ORM with Panache** (substituindo Spring Data JPA)
- **RESTEasy Reactive** (substituindo Spring MVC)
- **SmallRye JWT** (substituindo Spring Security + JJWT)
- **H2 Database** (in-memory para desenvolvimento e testes)
- **Postman Collection** (documentação da API)
- **Maven 3.9.6**
- **Docker** (containerização)

## 📋 Principais Mudanças na Migração

### De Spring Boot para Quarkus

| Componente | Spring Boot | Quarkus |
|------------|-------------|---------|
| **Web Framework** | Spring MVC (`@RestController`) | RESTEasy Reactive (`@Path`, `@GET`, `@POST`) |
| **Injeção de Dependência** | `@Autowired`, `@RequiredArgsConstructor` | `@Inject` (CDI) |
| **Persistência** | Spring Data JPA (`JpaRepository`) | Hibernate Panache (Active Record) |
| **Segurança** | Spring Security + JJWT | SmallRye JWT (MicroProfile JWT) |
| **Configuração** | `application.yml` | `application.properties` |
| **Validação** | `jakarta.validation` | `jakarta.validation` (sem mudanças) |
| **Documentação API** | SpringDoc OpenAPI | Postman Collection |

### Hibernate Panache - Active Record Pattern

As entidades agora estendem `PanacheEntity` e não precisam mais de repositórios separados:

```java
// Antes (Spring Data JPA)
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(String cpf);
}

// Depois (Panache)
// Sem repository! Métodos diretos na entidade:
Client client = Client.find("cpf", cpf).firstResult();
List<Client> clients = Client.listAll();
client.persist();
```

### REST Endpoints - JAX-RS

```java
// Antes (Spring MVC)
@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}

// Depois (JAX-RS/RESTEasy)
@Path("/api/clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientController {
    @GET
    @Path("/{id}")
    public Response getClient(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }
}
```

### Segurança JWT

```java
// Antes (Spring Security)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configuração complexa com filtros
}

// Depois (SmallRye JWT)
// Configuração via application.properties
// mp.jwt.verify.issuer=painel-investimentos
@RolesAllowed({"USER", "ADMIN"})
public class InvestmentController { }
```

## 🔧 Configuração

### Requisitos

- Java 21+
- Maven 3.9+
- Docker (opcional)

### Executar Aplicação

#### Modo Development (com live reload)

```bash
./mvnw compile quarkus:dev
```

#### Modo Production

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

#### Docker

```bash
docker-compose up --build
```

### Compilação Nativa (GraalVM)

```bash
./mvnw package -Pnative
./target/painel-investimentos-1.0.0-runner
```

## 📚 Endpoints da API

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login` | Login e geração de token JWT |

### Investimentos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/simular-investimento` | Simular investimento |
| GET | `/api/v1/simulacoes` | Histórico de simulações |
| GET | `/api/v1/simulacoes/por-produto-dia` | Simulações agrupadas |
| GET | `/api/v1/perfil-risco/{clienteId}` | Perfil de risco do cliente |
| GET | `/api/v1/produtos-recomendados/{perfil}` | Produtos recomendados |
| GET | `/api/v1/investimentos/{clienteId}` | Histórico de investimentos |
| GET | `/api/v1/telemetria` | Dados de telemetria |

### Health Check

- http://localhost:8081/api/health

## 🧪 Testes

```bash
# Executar testes
./mvnw test

# Executar com cobertura
./mvnw verify

# Relatório JaCoCo
./mvnw jacoco:report
```

## 📊 Code Quality

### SonarCloud

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=alfredosantos83_Painel-de-Investimentos&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=alfredosantos83_Painel-de-Investimentos)

```bash
# Análise local
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=alfredosantos83_Painel-de-Investimentos \
  -Dsonar.organization=alfredosantos83-2 \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=YOUR_TOKEN
```

## 🐳 Docker

### Imagens Disponíveis

- **JVM Mode**: `painel-investimentos:jvm` (mais rápido para build)
- **Native Mode**: `painel-investimentos:native` (menor tamanho, startup instantâneo)

```bash
# Build JVM
docker build -f src/main/docker/Dockerfile.jvm -t painel-investimentos:jvm .

# Build Native
docker build -f src/main/docker/Dockerfile.native -t painel-investimentos:native .
```

## 📝 Variáveis de Ambiente

| Variável | Descrição | Default |
|----------|-----------|---------|
| `QUARKUS_HTTP_PORT` | Porta HTTP | 8081 |
| `JWT_SECRET` | Secret para JWT | (valor padrão) |
| `JWT_EXPIRATION` | Tempo de expiração JWT (ms) | 86400000 (24h) |
| `QUARKUS_DATASOURCE_JDBC_URL` | URL do banco | jdbc:h2:mem:investimentos |

## 🎯 Benefícios da Migração para Quarkus

1. **Startup Ultra-Rápido**: ~0.5s no modo JVM, ~0.01s no modo nativo
2. **Menor Consumo de Memória**: ~30-50% menos RAM que Spring Boot
3. **Live Reload**: Mudanças refletidas instantaneamente no modo dev
4. **Compilação Nativa**: GraalVM para containers ainda menores
5. **Cloud Native**: Otimizado para Kubernetes e serverless
6. **Dev UI**: Interface de desenvolvimento em http://localhost:8081/q/dev
7. **Standards-Based**: Baseado em MicroProfile e Jakarta EE

## 📦 Build & Deploy

### Build Local

```bash
# Limpar e compilar
./mvnw clean package

# Pular testes
./mvnw clean package -DskipTests

# Build nativo
./mvnw clean package -Pnative
```

### CI/CD (GitHub Actions)

O projeto inclui workflows para:
- Build e testes automáticos
- Análise de código com SonarCloud  
- Deploy automatizado (configure seu ambiente)

## 📖 Documentação Adicional

- [Quarkus Getting Started](https://quarkus.io/guides/getting-started)
- [Hibernate Panache Guide](https://quarkus.io/guides/hibernate-orm-panache)
- [SmallRye JWT Guide](https://quarkus.io/guides/security-jwt)
- [RESTEasy Reactive Guide](https://quarkus.io/guides/resteasy-reactive)

## 🔄 Migration Notes

Esta aplicação foi migrada de **Spring Boot 3.5.0** para **Quarkus 3.8.6 LTS** mantendo todas as funcionalidades:

- ✅ Autenticação JWT RS256 com SmallRye JWT
- ✅ Validação de entrada (Jakarta Validation)
- ✅ Persistência com Hibernate ORM Panache
- ✅ Endpoints REST com RESTEasy Reactive
- ✅ Documentação via Postman Collection
- ✅ Health checks (liveness/readiness)
- ✅ Banco H2 in-memory
- ✅ Suporte a Docker (JVM e Native)
- ✅ 187 testes unitários e integração (100%)
- ✅ Cobertura de código: 97.3% (IntelliJ IDEA Coverage)

## 📄 Licença

Este projeto é privado e de uso interno.

---

**Desenvolvido com Quarkus** - Supersonic Subatomic Java ⚡
