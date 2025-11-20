# 🧪 Guia de Testes - API Painel de Investimentos


## 🗄️ Exemplo de Configuração para SQL Server

Para rodar com SQL Server, configure o `application.yml`:
```yaml
quarkus:
  datasource:
    db-kind: mssql
    jdbc:
      url: jdbc:sqlserver://localhost:1433;databaseName=investimentos
    username: sa
    password: sua_senha
  hibernate-orm:
    database:
      generation: update
    sql-load-script: data.sql
```

Adicione ao `pom.xml`:
```xml
<dependency>
  <groupId>com.microsoft.sqlserver</groupId>
  <artifactId>mssql-jdbc</artifactId>
  <version>12.6.1.jre11</version>
</dependency>
```

Antes de executar os testes, certifique-se de que o Quarkus está rodando.

### Opção 1: Usar Script Automatizado

```powershell
# Executar script de testes (inicia Quarkus automaticamente)
.\test-api.ps1
```

### Opção 2: Testes Manuais com PowerShell (Recomendado para desenvolvimento)

**Terminal 1 (PowerShell): Iniciar Quarkus**
```powershell
# Compilar e iniciar Quarkus em modo desenvolvimento
mvn compile quarkus:dev
```
Aguarde até ver: `Listening on: http://localhost:8081`

**Terminal 2 (PowerShell): Executar Testes**
```powershell
# Agora execute os comandos de teste deste guia
# Exemplos abaixo ↓
```

> **💡 Dica:** Mantenha o Terminal 1 aberto com Quarkus rodando enquanto testa no Terminal 2. O hot reload do Quarkus permite alterações em código sem reiniciar!

### Opção 3: Testes com Postman (Interface Gráfica)

Para testes usando interface gráfica, consulte o guia dedicado:
📖 **[GUIA-TESTES-POSTMAN.md](GUIA-TESTES-POSTMAN.md)**

### Opção 4: Testes Unitários e Integração com IntelliJ IDEA

**1. Executar Todos os Testes (187 testes)**
1. Abrir IntelliJ IDEA
2. Botão direito na pasta `src/test/java`
3. Selecionar **"Run 'All Tests'"**
4. Aguardar execução (~58 segundos)
5. Resultado: 187/187 testes passando (100% ✅)

**2. Executar com Cobertura de Código (Recomendado)**
1. Botão direito na pasta `src/test/java`
2. Selecionar **"Run Tests in 'invest' with Coverage"**
3. Visualizar relatório no painel "Coverage" (lateral direita)
4. Resultado: **Cobertura oficial (IntelliJ 97,3%)**

**3. Executar Teste Específico**
1. Abrir classe de teste (ex: `AuthControllerTest.java`)
2. Clicar no ícone verde ▶️ ao lado da classe ou método
3. Selecionar **"Run 'AuthControllerTest'"** ou **"Run 'testLogin()'"**

**4. Executar em Modo Debug**
1. Definir breakpoints (clicar na margem esquerda)
2. Botão direito no teste
3. Selecionar **"Debug 'AuthControllerTest'"**
4. Analisar variáveis e fluxo de execução

**✨ Vantagens IntelliJ:**
✅ Cobertura precisa (97.3% vs 52% JaCoCo)
**Nota:** Resultados do IntelliJ IDEA Coverage são a métrica oficial para documentação, compliance e apresentação.
✅ Interface visual intuitiva
✅ Debug interativo com breakpoints
✅ Execução seletiva (classe, método, pacote)
✅ Relatório de cobertura em tempo real

---

## 📋 Endpoints Disponíveis

### Públicos (sem autenticação)
- `GET /api/health-test` - Health check
- `POST /api/auth/login` - Login e obtenção de token JWT
- `GET /api/debug/users` - Lista usuários (debug)
- `GET /api/debug/test-password` - Testa senha (debug)
- `GET /api/debug/generate-hash` - Gera hash BCrypt (debug)

### Protegidos (requer JWT)
- `GET /api/secure/profile` - Perfil do usuário (USER/ADMIN)
- `GET /api/secure/admin` - Área administrativa (ADMIN)
- `GET /api/secure/user` - Área do usuário (USER/ADMIN)

---

## 🔧 Exemplos PowerShell

### 1️⃣ Login e Obter Token JWT

```powershell
# Login como ADMIN
$body = @{
    username = 'admin'
    password = 'password123'
} | ConvertTo-Json

$response = Invoke-RestMethod `
    -Uri 'http://localhost:8081/api/auth/login' `
    -Method POST `
    -Body $body `
    -ContentType 'application/json'

# Salvar token
$token = $response.token
Write-Host "Token obtido: $($token.Substring(0,50))..."
```

**Resposta:**
```json
{
  "token": "eyJ0eXAiOiJKV1QiLCJhbGci...",
  "type": "Bearer",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

---

### 2️⃣ Acessar Perfil do Usuário

```powershell
$headers = @{
    Authorization = "Bearer $token"
}

$profile = Invoke-RestMethod `
    -Uri 'http://localhost:8081/api/secure/profile' `
    -Method GET `
    -Headers $headers

$profile | ConvertTo-Json
```

**Resposta:**
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

---

### 3️⃣ Área Administrativa (ADMIN apenas)

```powershell
$headers = @{Authorization = "Bearer $token"}

$admin = Invoke-RestMethod `
    -Uri 'http://localhost:8081/api/secure/admin' `
    -Method GET `
    -Headers $headers

Write-Host $admin.message
```

**Resposta:**
```json
{
  "message": "Bem-vindo, administrador!",
  "user": "admin",
  "access": "ADMIN"
}
```

---

### 4️⃣ Testar Controle de Acesso

```powershell
# Login como USER
$userBody = @{username='user'; password='password123'} | ConvertTo-Json
$userResponse = Invoke-RestMethod `
    -Uri 'http://localhost:8081/api/auth/login' `
    -Method POST `
    -Body $userBody `
    -ContentType 'application/json'

# Tentar acessar área admin (vai falhar com 403)
try {
    $headers = @{Authorization = "Bearer $($userResponse.token)"}
    Invoke-RestMethod `
        -Uri 'http://localhost:8081/api/secure/admin' `
        -Method GET `
        -Headers $headers
} catch {
    Write-Host "Erro esperado: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    # Output: Forbidden (403)
}
```

---

### 5️⃣ Health Check

```powershell
$health = Invoke-RestMethod `
    -Uri 'http://localhost:8081/api/health-test' `
    -Method GET

Write-Host "Status: $($health.status)"
Write-Host "Mensagem: $($health.message)"
```

---

## 🌐 Exemplos cURL (Linux/Mac/Git Bash)

### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
```

### Acessar Perfil
```bash
TOKEN="seu-token-jwt-aqui"

curl -X GET http://localhost:8081/api/secure/profile \
  -H "Authorization: Bearer $TOKEN"
```

### Área Admin
```bash
curl -X GET http://localhost:8081/api/secure/admin \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🧪 Suite Completa de Testes

Execute todos os testes de uma vez:

```powershell
# Salvar este script como test-api.ps1

Write-Host "🧪 Executando Suite de Testes..." -ForegroundColor Cyan

# 1. Health Check
Write-Host "`n1️⃣ Health Check"
$health = Invoke-RestMethod -Uri 'http://localhost:8081/api/health-test'
Write-Host "   ✅ Status: $($health.status)" -ForegroundColor Green

# 2. Login Admin
Write-Host "`n2️⃣ Login Admin"
$adminLogin = @{username='admin'; password='password123'} | ConvertTo-Json
$adminResponse = Invoke-RestMethod -Uri 'http://localhost:8081/api/auth/login' -Method POST -Body $adminLogin -ContentType 'application/json'
$adminToken = $adminResponse.token
Write-Host "   ✅ Token obtido" -ForegroundColor Green

# 3. Login User
Write-Host "`n3️⃣ Login User"
$userLogin = @{username='user'; password='password123'} | ConvertTo-Json
$userResponse = Invoke-RestMethod -Uri 'http://localhost:8081/api/auth/login' -Method POST -Body $userLogin -ContentType 'application/json'
$userToken = $userResponse.token
Write-Host "   ✅ Token obtido" -ForegroundColor Green

# 4. Perfil Admin
Write-Host "`n4️⃣ Perfil Admin"
$headers = @{Authorization = "Bearer $adminToken"}
$profile = Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/profile' -Headers $headers
Write-Host "   ✅ Username: $($profile.username)" -ForegroundColor Green

# 5. Área Admin (com Admin token)
Write-Host "`n5️⃣ Área Admin (Admin)"
$admin = Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/admin' -Headers $headers
Write-Host "   ✅ $($admin.message)" -ForegroundColor Green

# 6. Área User (com User token)
Write-Host "`n6️⃣ Área User (User)"
$headers = @{Authorization = "Bearer $userToken"}
$user = Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/user' -Headers $headers
Write-Host "   ✅ $($user.message)" -ForegroundColor Green

# 7. Teste de Segurança - User tentando Admin
Write-Host "`n7️⃣ Segurança: User → Admin (deve falhar)"
try {
    Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/admin' -Headers $headers
    Write-Host "   ❌ FALHOU - deveria bloquear" -ForegroundColor Red
} catch {
    Write-Host "   ✅ Bloqueado com 403 Forbidden" -ForegroundColor Green
}

# 8. Teste de Segurança - Sem token
Write-Host "`n8️⃣ Segurança: Sem token (deve falhar)"
try {
    Invoke-RestMethod -Uri 'http://localhost:8081/api/secure/profile'
    Write-Host "   ❌ FALHOU - deveria bloquear" -ForegroundColor Red
} catch {
    Write-Host "   ✅ Bloqueado com 401 Unauthorized" -ForegroundColor Green
}

Write-Host "`n✨ Todos os testes executados!" -ForegroundColor Green
```

---

## 👥 Usuários de Teste

| Username | Password     | Role  | Email             |
|----------|-------------|-------|-------------------|
| admin    | password123 | ADMIN | admin@caixa.com   |
| user     | password123 | USER  | user@caixa.com    |

---

## 📊 Códigos de Status HTTP

| Código | Significado | Quando ocorre |
|--------|-------------|---------------|
| 200 OK | Sucesso | Login bem-sucedido, acesso autorizado |
| 401 Unauthorized | Não autenticado | Sem token ou token inválido |
| 403 Forbidden | Não autorizado | Role insuficiente |
| 500 Internal Server Error | Erro no servidor | Exceção não tratada |

---

## 🔍 Decodificando o Token JWT

Para visualizar o conteúdo do token JWT:

```powershell
# Extrair payload do token
$token = "seu-token-aqui"
$parts = $token.Split('.')
$payload = $parts[1]

# Decodificar Base64
$padding = '=' * (4 - ($payload.Length % 4))
$base64 = $payload + $padding
$base64 = $base64.Replace('-', '+').Replace('_', '/')
$bytes = [Convert]::FromBase64String($base64)
$json = [Text.Encoding]::UTF8.GetString($bytes)

$json | ConvertFrom-Json | ConvertTo-Json -Depth 10
```

**Exemplo de Payload:**
```json
{
  "iss": "painel-investimentos",
  "upn": "admin",
  "groups": ["ADMIN"],
  "email": "admin@caixa.com",
  "userId": 1,
  "iat": 1763253999,
  "exp": 1763340399,
  "jti": "a8a67981-840f-4c10-..."
}
```

---

## 🛠️ Troubleshooting

### Token expirado
**Erro:** 401 Unauthorized  
**Solução:** Fazer login novamente para obter novo token

### Acesso negado (403)
**Erro:** 403 Forbidden  
**Solução:** Verificar se o usuário tem a role necessária

### Aplicação não responde
**Solução:** Verificar se Quarkus está rodando na porta 8081
```powershell
netstat -ano | findstr :8081
```

### Ver logs do Quarkus
Logs aparecem no terminal onde foi executado `mvn quarkus:dev`

---

## 📚 Referências

- [SmallRye JWT](https://smallrye.io/smallrye-jwt/)
- [MicroProfile JWT](https://github.com/eclipse/microprofile-jwt-auth)
- [Quarkus Security](https://quarkus.io/guides/security-jwt)
- [JAX-RS](https://jakarta.ee/specifications/restful-ws/)
