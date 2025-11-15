# Script para executar análise do SonarQube local
# Uso: .\run-sonarqube.ps1

Write-Host "=== SonarQube Local Analysis ===" -ForegroundColor Cyan
Write-Host ""

# Verificar se Docker está disponível
try {
    docker --version | Out-Null
    Write-Host "✓ Docker encontrado" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker não encontrado. Por favor, instale o Docker Desktop." -ForegroundColor Red
    exit 1
}

# Iniciar SonarQube
Write-Host ""
Write-Host "Iniciando SonarQube..." -ForegroundColor Yellow
docker-compose -f docker-compose-sonarqube.yml up -d

Write-Host ""
Write-Host "Aguardando SonarQube inicializar (isso pode levar 1-2 minutos)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Verificar se SonarQube está pronto
$maxAttempts = 20
$attempt = 0
$isReady = $false

while ($attempt -lt $maxAttempts -and -not $isReady) {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:9000/api/system/status" -UseBasicParsing -TimeoutSec 5
        $status = ($response.Content | ConvertFrom-Json).status
        if ($status -eq "UP") {
            $isReady = $true
            Write-Host "✓ SonarQube está pronto!" -ForegroundColor Green
        }
    } catch {
        Write-Host "." -NoNewline
        Start-Sleep -Seconds 5
        $attempt++
    }
}

if (-not $isReady) {
    Write-Host ""
    Write-Host "✗ SonarQube não iniciou a tempo. Verifique os logs com: docker logs sonarqube-local" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "=== Configuração Inicial ===" -ForegroundColor Cyan
Write-Host "1. Acesse: http://localhost:9000" -ForegroundColor Yellow
Write-Host "2. Login padrão: admin / admin" -ForegroundColor Yellow
Write-Host "3. Você será solicitado a alterar a senha" -ForegroundColor Yellow
Write-Host "4. Crie um projeto manual com a chave: painel-investimentos" -ForegroundColor Yellow
Write-Host "5. Gere um token de análise" -ForegroundColor Yellow
Write-Host ""
Write-Host "Após configurar, execute:" -ForegroundColor Cyan
Write-Host "mvn clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=SEU_TOKEN -Dsonar.projectKey=painel-investimentos" -ForegroundColor White
Write-Host ""
Write-Host "Para parar o SonarQube:" -ForegroundColor Cyan
Write-Host "docker-compose -f docker-compose-sonarqube.yml down" -ForegroundColor White
