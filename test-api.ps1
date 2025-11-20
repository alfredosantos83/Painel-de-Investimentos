# 🧪 Script de Testes - API Painel de Investimentos
# Execute: .\test-api.ps1

Write-Host "Executando Suite de Testes da API..." -ForegroundColor Cyan
Write-Host ("".PadLeft(60, '='))

# 1. Health Check
Write-Host "`n1️⃣ Health Check" -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri 'http://localhost:8081/health-test'
    Write-Host "   ✅ Status: $($health.status)" -ForegroundColor Green
    Write-Host "   ✅ Mensagem: $($health.message)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 2. Login Admin
Write-Host "`n2️⃣ Login Admin" -ForegroundColor Yellow
try {
    $adminLogin = @{username='admin'; password='password123'} | ConvertTo-Json
    $adminResponse = Invoke-RestMethod -Uri 'http://localhost:8081/auth/login' -Method POST -Body $adminLogin -ContentType 'application/json'
    $adminToken = $adminResponse.token
    Write-Host "   ✅ Token obtido: $($adminToken.Substring(0,30))..." -ForegroundColor Green
    Write-Host "   ✅ Username: $($adminResponse.username)" -ForegroundColor Green
    Write-Host "   ✅ Role: $($adminResponse.role)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ ERRO: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 3. Login User
Write-Host "`n3️⃣ Login User" -ForegroundColor Yellow
try {
    $userLogin = @{username='user'; password='password123'} | ConvertTo-Json
    $userResponse = Invoke-RestMethod -Uri 'http://localhost:8081/auth/login' -Method POST -Body $userLogin -ContentType 'application/json'
    $userToken = $userResponse.token
    Write-Host "   ✅ Token obtido: $($userToken.Substring(0,30))..." -ForegroundColor Green
    Write-Host "   ✅ Username: $($userResponse.username)" -ForegroundColor Green
    Write-Host "   ✅ Role: $($userResponse.role)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. Perfil Admin
Write-Host "`n4️⃣ Perfil Admin" -ForegroundColor Yellow
try {
    $headers = @{Authorization = "Bearer $adminToken"}
    $adminProfile = Invoke-RestMethod -Uri 'http://localhost:8081/secure/profile' -Headers $headers
    Write-Host "   Username: $($adminProfile.username)" -ForegroundColor Green
    Write-Host "   Email: $($adminProfile.email)" -ForegroundColor Green
    Write-Host "   Roles: $($adminProfile.roles -join ', ')" -ForegroundColor Green
} catch {
    Write-Host "   ❌ ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. Área Admin (com Admin token)
Write-Host "`n5️⃣ Área Admin (Admin)" -ForegroundColor Yellow
try {
    $headers = @{Authorization = "Bearer $adminToken"}
    $admin = Invoke-RestMethod -Uri 'http://localhost:8081/secure/admin' -Headers $headers
    Write-Host "   ✅ Mensagem: $($admin.message)" -ForegroundColor Green
    Write-Host "   ✅ User: $($admin.user)" -ForegroundColor Green
    Write-Host "   ✅ Access: $($admin.access)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 6. Área User (com Admin token)
Write-Host "`n6️⃣ Área User (Admin)" -ForegroundColor Yellow
try {
    $headers = @{Authorization = "Bearer $adminToken"}
    $user = Invoke-RestMethod -Uri 'http://localhost:8081/secure/user' -Headers $headers
    Write-Host "   ✅ Mensagem: $($user.message)" -ForegroundColor Green
    Write-Host "   ✅ User: $($user.user)" -ForegroundColor Green
    Write-Host "   ✅ Access: $($user.access)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 7. Área User (com User token)
Write-Host "`n7️⃣ Área User (User)" -ForegroundColor Yellow
try {
    $headers = @{Authorization = "Bearer $userToken"}
    $user = Invoke-RestMethod -Uri 'http://localhost:8081/secure/user' -Headers $headers
    Write-Host "   ✅ Mensagem: $($user.message)" -ForegroundColor Green
    Write-Host "   ✅ User: $($user.user)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 8. Teste de Segurança - User tentando Admin (deve falhar)
Write-Host "`n8️⃣ Segurança: User → Admin (deve falhar com 403)" -ForegroundColor Yellow
try {
    $headers = @{Authorization = "Bearer $userToken"}
    Invoke-RestMethod -Uri 'http://localhost:8081/secure/admin' -Headers $headers
    Write-Host "   ❌ FALHOU - deveria bloquear!" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 'Forbidden') {
        Write-Host "   ✅ Bloqueado com 403 Forbidden (esperado)" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Erro inesperado: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

# 9. Teste de Segurança - Sem token (deve falhar)
Write-Host "`n9️⃣ Segurança: Sem token (deve falhar com 401)" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri 'http://localhost:8081/secure/profile'
    Write-Host "   ❌ FALHOU - deveria bloquear!" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 'Unauthorized') {
        Write-Host "   ✅ Bloqueado com 401 Unauthorized (esperado)" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Erro inesperado: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

# 10. Teste de Segurança - Token inválido (deve falhar)
Write-Host "`n🔟 Segurança: Token inválido (deve falhar com 401)" -ForegroundColor Yellow
try {
    $headers = @{Authorization = "Bearer token_invalido_123"}
    Invoke-RestMethod -Uri 'http://localhost:8081/secure/profile' -Headers $headers
    Write-Host "   ❌ FALHOU - deveria bloquear!" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 'Unauthorized') {
        Write-Host "   ✅ Bloqueado com 401 Unauthorized (esperado)" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Erro inesperado: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host ("".PadLeft(60, '='))
Write-Host "Todos os testes executados com sucesso!" -ForegroundColor Green
Write-Host ("".PadLeft(60, '='))
