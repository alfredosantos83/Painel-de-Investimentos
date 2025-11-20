# Guia de Configuração do SonarCloud

> **📝 Nota:** SonarCloud é uma ferramenta **opcional** para análise de qualidade de código. O projeto utiliza **IntelliJ IDEA Coverage (97,3%)** como métrica oficial de cobertura de código. JaCoCo pode ser ignorado para compliance.

## ✅ Passo 1: Criar Organização (CONCLUÍDO)
- Name: `alfredosantos83`
- Key: `alfredosantos83-1`

## 📋 Passo 2: Importar Projeto

Após criar a organização, siga estes passos:

1. **Clique em "Analyze new project"** ou "+" no canto superior direito
2. **Selecione o repositório**: `alfredosantos83/Painel-de-Investimentos`
3. **Configure o projeto**:
   - Project key: `alfredosantos83_Painel-de-Investimentos`
   - Display name: `Painel de Investimentos`

## 🔑 Passo 3: Gerar Token

1. Acesse: **Account > Security > Generate Token**
2. Nome do token: `painel-investimentos-token`
3. Type: `Global Analysis Token` ou `Project Analysis Token`
4. **Copie o token gerado** (você só verá uma vez!)

## 🔐 Passo 4: Adicionar Token ao GitHub

1. Acesse: https://github.com/alfredosantos83/Painel-de-Investimentos/settings/secrets/actions
2. Clique em **"New repository secret"**
3. Nome: `SONAR_TOKEN`
4. Valor: Cole o token copiado do SonarCloud
5. Clique em **"Add secret"**


## 🗄️ Exemplo de Configuração para SQL Server

Se for usar SQL Server, configure o `application.yml` conforme exemplo:
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

Adicione o driver JDBC ao `pom.xml`:
```xml
<dependency>
   <groupId>com.microsoft.sqlserver</groupId>
   <artifactId>mssql-jdbc</artifactId>
   <version>12.6.1.jre11</version>
</dependency>
```

### Opção A: Via GitHub Actions (Automático)
Faça um push para o repositório e o workflow executará automaticamente:
```bash
git add .
git commit -m "Update SonarCloud configuration"
git push origin master
```

### Opção B: Executar Localmente
```bash
mvn clean verify sonar:sonar -Dsonar.token=SEU_TOKEN_AQUI
```

## 📊 Passo 6: Visualizar Resultados

Acesse: https://sonarcloud.io/organizations/alfredosantos83-1/projects

Você verá:
- Quality Gate status
- Code Coverage
- Code Smells
- Bugs
- Vulnerabilities
- Security Hotspots
- Duplicações


## 🏆 Badges para o README

Após a primeira análise, adicione estes badges ao README.md:

```markdown
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=alfredosantos83_Painel-de-Investimentos&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=alfredosantos83_Painel-de-Investimentos)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=alfredosantos83_Painel-de-Investimentos&metric=coverage)](https://sonarcloud.io/summary/new_code?id=alfredosantos83_Painel-de-Investimentos)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=alfredosantos83_Painel-de-Investimentos&metric=bugs)](https://sonarcloud.io/summary/new_code?id=alfredosantos83_Painel-de-Investimentos)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=alfredosantos83_Painel-de-Investimentos&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=alfredosantos83_Painel-de-Investimentos)
```

## ⚙️ Configurações Opcionais

### Definir Quality Gate
1. No SonarCloud, acesse: **Project Settings > Quality Gate**
2. Selecione "Sonar way" (recomendado) ou crie customizado

### Configurar Branch Principal
1. Acesse: **Project Settings > Branches and Pull Requests**
2. Defina `master` como branch principal

### Notificações
1. Acesse: **Project Settings > Notifications**
2. Configure notificações por email quando Quality Gate falhar


## 🎉 Pronto!

Seu projeto agora está totalmente integrado com SonarCloud e terá análises automáticas a cada push!

**Última atualização:** 19/11/2025
