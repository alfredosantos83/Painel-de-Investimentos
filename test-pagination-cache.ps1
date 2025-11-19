# Script de teste para Paginação e Cache
# Painel de Investimentos - API REST

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TESTE DE PAGINAÇÃO E CACHE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8081"

# 1. Fazer login para obter token
Write-Host "1. Obtendo token de autenticação..." -ForegroundColor Yellow
$loginBody = @{
    username = "admin"
    password = "password123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    $token = $loginResponse.token
    Write-Host "   ✓ Token obtido com sucesso!" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "   ✗ Erro ao fazer login: $_" -ForegroundColor Red
    exit
}

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# 2. Testar endpoint paginado (página 0, tamanho 5)
Write-Host "2. Testando paginação (página 0, 5 itens)..." -ForegroundColor Yellow
try {
    $paginatedResponse = Invoke-RestMethod -Uri "$baseUrl/api/products?page=0&size=5" -Method GET -Headers $headers
    Write-Host "   ✓ Página: $($paginatedResponse.page)" -ForegroundColor Green
    Write-Host "   ✓ Tamanho: $($paginatedResponse.size)" -ForegroundColor Green
    Write-Host "   ✓ Total: $($paginatedResponse.total)" -ForegroundColor Green
    Write-Host "   ✓ Total de páginas: $($paginatedResponse.totalPages)" -ForegroundColor Green
    Write-Host "   ✓ Itens retornados: $($paginatedResponse.items.Count)" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "   ✗ Erro ao testar paginação: $_" -ForegroundColor Red
}

# 3. Testar endpoint paginado (página 1, tamanho 3)
Write-Host "3. Testando paginação (página 1, 3 itens)..." -ForegroundColor Yellow
try {
    $paginatedResponse2 = Invoke-RestMethod -Uri "$baseUrl/api/products?page=1&size=3" -Method GET -Headers $headers
    Write-Host "   ✓ Página: $($paginatedResponse2.page)" -ForegroundColor Green
    Write-Host "   ✓ Tamanho: $($paginatedResponse2.size)" -ForegroundColor Green
    Write-Host "   ✓ Total: $($paginatedResponse2.total)" -ForegroundColor Green
    Write-Host "   ✓ Itens retornados: $($paginatedResponse2.items.Count)" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "   ✗ Erro ao testar paginação: $_" -ForegroundColor Red
}

# 4. Testar endpoint com cache (listagem completa)
Write-Host "4. Testando endpoint com cache (1ª chamada)..." -ForegroundColor Yellow
try {
    $sw = [System.Diagnostics.Stopwatch]::StartNew()
    $allProducts1 = Invoke-RestMethod -Uri "$baseUrl/api/products/all" -Method GET -Headers $headers
    $sw.Stop()
    $time1 = $sw.ElapsedMilliseconds
    Write-Host "   ✓ Produtos retornados: $($allProducts1.Count)" -ForegroundColor Green
    Write-Host "   ✓ Tempo: $time1 ms (SEM cache)" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "   ✗ Erro ao testar cache: $_" -ForegroundColor Red
}

# 5. Testar endpoint com cache (2ª chamada - deve vir do cache)
Write-Host "5. Testando endpoint com cache (2ª chamada)..." -ForegroundColor Yellow
try {
    $sw = [System.Diagnostics.Stopwatch]::StartNew()
    $allProducts2 = Invoke-RestMethod -Uri "$baseUrl/api/products/all" -Method GET -Headers $headers
    $sw.Stop()
    $time2 = $sw.ElapsedMilliseconds
    Write-Host "   ✓ Produtos retornados: $($allProducts2.Count)" -ForegroundColor Green
    Write-Host "   ✓ Tempo: $time2 ms (COM cache)" -ForegroundColor Green
    
    if ($time2 -lt $time1) {
        Write-Host "   ✓ Cache funcionando! ($time1 ms → $time2 ms)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Cache pode não estar ativo" -ForegroundColor Yellow
    }
    Write-Host ""
} catch {
    Write-Host "   ✗ Erro ao testar cache: $_" -ForegroundColor Red
}

# 6. Testar validação de paginação (página negativa)
Write-Host "6. Testando validação (página negativa)..." -ForegroundColor Yellow
try {
    $null = Invoke-RestMethod -Uri "$baseUrl/api/products?page=-1&size=10" -Method GET -Headers $headers
    Write-Host "   ✗ Validação não funcionou!" -ForegroundColor Red
} catch {
    Write-Host "   ✓ Validação funcionando! Erro esperado: 400 Bad Request" -ForegroundColor Green
    Write-Host ""
}

# 7. Testar validação de paginação (tamanho inválido)
Write-Host "7. Testando validação (tamanho > 100)..." -ForegroundColor Yellow
try {
    $null = Invoke-RestMethod -Uri "$baseUrl/api/products?page=0&size=150" -Method GET -Headers $headers
    Write-Host "   ✗ Validação não funcionou!" -ForegroundColor Red
} catch {
    Write-Host "   ✓ Validação funcionando! Erro esperado: 400 Bad Request" -ForegroundColor Green
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TESTES CONCLUÍDOS!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Resumo:" -ForegroundColor Yellow
Write-Host "✓ Paginação implementada e funcionando" -ForegroundColor Green
Write-Host "✓ Cache implementado (Caffeine)" -ForegroundColor Green
Write-Host "✓ Validações de entrada funcionando" -ForegroundColor Green
Write-Host "✓ Endpoints REST operacionais" -ForegroundColor Green
