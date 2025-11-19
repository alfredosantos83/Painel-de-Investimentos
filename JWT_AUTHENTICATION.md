# Autenticação JWT - Painel de Investimentos

## 📋 Resumo da Implementação

A aplicação agora possui **autenticação JWT completa** usando **SmallRye JWT** do Quarkus com chaves RSA 2048 bits.

## 🔐 Configuração JWT

### Chaves RSA Geradas
- **Chave Pública**: `src/main/resources/META-INF/resources/publicKey.pem`
- **Chave Privada**: `src/main/resources/privateKey.pem`
- **Algoritmo**: RS256 (RSA 2048 bits)
- **Validade do Token**: 24 horas (86400000 ms)

### Propriedades de Configuração
```properties
mp.jwt.verify.publickey.location=META-INF/resources/publicKey.pem
mp.jwt.verify.issuer=painel-investimentos
smallrye.jwt.sign.key.location=privateKey.pem
jwt.expiration=86400000
```

## 🚀 Endpoints Disponíveis

### 1. Autenticação (Público)
#### POST `/auth/login`
Autentica usuário e retorna token JWT.

**Request:**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJ0eXAiOiJKV1QiLCJhbGci...",
  "type": "Bearer",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

### 2. Endpoints Protegidos

#### GET `/api/secure/profile` 
**Roles**: USER, ADMIN  
Retorna perfil do usuário autenticado.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
  "username": "admin",
  "email": "admin@caixa.com",
  "userId": 1,
  "roles": ["ADMIN"],
  "issuer": "painel-investimentos",
  "expiresAt": 1763340399,
  "issuedAt": 1763253999
}
```

#### GET `/api/secure/admin`
**Roles**: ADMIN apenas  
Endpoint exclusivo para administradores.

#### GET `/api/secure/user`
**Roles**: USER, ADMIN  
Área geral de usuários autenticados.

### 3. Endpoints Públicos (Debug)
- GET `/api/health-test` - Health check
- GET `/api/debug/users` - Lista usuários
- GET `/api/debug/test-password` - Testa validação de senha
- GET `/api/debug/generate-hash` - Gera hash BCrypt

## 👥 Usuários de Teste

| Username | Password     | Role  | Email             |
|----------|-------------|-------|-------------------|
| admin    | password123 | ADMIN | admin@caixa.com   |
| user     | password123 | USER  | user@caixa.com    |

## 🧪 Exemplos de Teste (PowerShell)

### 1. Login e Obter Token
```powershell
$body = @{username='admin'; password='password123'} | ConvertTo-Json
$response = Invoke-RestMethod -Uri 'http://localhost:8081/auth/login' `
    -Method POST -Body $body -ContentType 'application/json'
$token = $response.token
```

### 2. Acessar Endpoint Protegido
```powershell
$headers = @{Authorization = "Bearer $token"}
Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/profile' `
    -Method GET -Headers $headers
```

### 3. Testar Controle de Acesso
```powershell
# Admin acessando área admin - OK
$headers = @{Authorization = "Bearer $adminToken"}
Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/admin' `
    -Method GET -Headers $headers

# User tentando acessar área admin - ERRO 403
$headers = @{Authorization = "Bearer $userToken"}
Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/admin' `
    -Method GET -Headers $headers
```

## ✅ Testes Realizados

| Teste | Status | Descrição |
|-------|--------|-----------|
| Login com JWT | ✅ PASSOU | Token RS256 gerado com sucesso |
| Acesso autenticado | ✅ PASSOU | Endpoint protegido respondeu corretamente |
| Acesso sem token | ✅ PASSOU | Retornou 401 Unauthorized |
| Controle de roles (ADMIN) | ✅ PASSOU | Somente ADMIN acessa endpoint admin |
| Controle de roles (USER) | ✅ PASSOU | USER bloqueado em área admin (403) |
| Acesso USER permitido | ✅ PASSOU | USER acessa endpoints USER/ADMIN |
| Validação de senha BCrypt | ✅ PASSOU | Hash força 12 validando corretamente |

## 🔧 Estrutura de Segurança

### Classes Principais
- **JwtTokenProvider**: Geração de tokens JWT com chaves RSA
- **AuthService**: Autenticação de usuários com BCrypt
- **PasswordEncoder**: Encoding/validação com BCrypt força 12
- **SecureController**: Endpoints protegidos com @RolesAllowed
- **AuthController**: Login e autenticação

### Anotações de Segurança
```java
@RolesAllowed({"USER", "ADMIN"})  // Múltiplas roles
@RolesAllowed("ADMIN")            // Apenas ADMIN
```

### Injeção de JWT
```java
@Inject
JsonWebToken jwt;

String username = jwt.getName();
String email = jwt.getClaim("email");
Set<String> roles = jwt.getGroups();
```

## 📊 Claims no Token JWT

| Claim | Descrição | Exemplo |
|-------|-----------|---------|
| iss | Issuer | painel-investimentos |
| upn | Username (principal) | admin |
| groups | Roles do usuário | ["ADMIN"] |
| email | Email do usuário | admin@caixa.com |
| userId | ID do usuário | 1 |
| iat | Issued at (timestamp) | 1763253999 |
| exp | Expiration (timestamp) | 1763340399 |
| jti | JWT ID (único) | uuid |

## 🔄 Alternativas de Autenticação

### OAuth2 (Implementação Futura)
Para integrar OAuth2, adicionar:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-oidc</artifactId>
</dependency>
```

### Keycloak (Implementação Futura)
Para integrar Keycloak:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-keycloak-authorization</artifactId>
</dependency>
```

Configuração:
```properties
quarkus.oidc.auth-server-url=http://localhost:8080/realms/quarkus
quarkus.oidc.client-id=backend-service
quarkus.oidc.credentials.secret=secret
```

## 🎯 Vantagens da Implementação Atual

✅ **Segurança**: Chaves RSA 2048 bits  
✅ **Performance**: JWT stateless (sem consulta ao banco)  
✅ **Escalabilidade**: Tokens podem ser validados em qualquer instância  
✅ **Padrão**: MicroProfile JWT conforme especificação  
✅ **Integração**: Fácil migração para OAuth2/Keycloak no futuro  
✅ **Controle**: RBAC (Role-Based Access Control) implementado  

## 📝 Próximos Passos

1. ✅ JWT com RSA implementado
2. ⏳ Implementar refresh tokens
3. ⏳ Adicionar OAuth2/OIDC
4. ⏳ Integração com Keycloak
5. ⏳ Logout e blacklist de tokens
6. ⏳ Rate limiting por usuário
7. ⏳ Auditoria de acessos
