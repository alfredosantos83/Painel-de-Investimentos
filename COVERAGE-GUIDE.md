# 📊 Guia de Cobertura de Código

## 🎯 Opções de Cobertura para Projeto Quarkus

### ✅ Opção 1: IntelliJ IDEA Coverage (Recomendado)
**Vantagens:**
- ✅ Compatível nativamente com Quarkus CDI/AOP/Panache
- ✅ Sem avisos de bytecode mismatch
- ✅ Interface visual rica com drill-down
- ✅ Cobertura mais precisa que JaCoCo

**Como usar:**
1. Abra o projeto no IntelliJ IDEA
2. Clique com botão direito em `src/test/java`
3. Selecione **"Run 'All Tests' with Coverage"**
4. Aguarde execução dos 187 testes
5. Visualize relatório interativo na IDE

**Exportar relatório HTML:**
- Tools → Generate Coverage Report → Escolha pasta de destino

---

### ⚙️ Opção 2: JaCoCo via Maven (Atual)
**Vantagens:**
✅ Integrado ao build Maven
✅ Geração automática na fase `verify`
✅ Compatível com SonarQube
⚠️ Avisos de bytecode (normais com Quarkus)

**Comandos:**
```bash
# Gerar relatório completo
mvn clean verify

# Apenas relatório (sem recompilar)
mvn jacoco:report

# Abrir relatório HTML
start target/site/jacoco/index.html
```

**Relatório HTML:** `target/site/jacoco/index.html`

**Nota sobre avisos:**
Os avisos de bytecode mismatch são **NORMAIS** e **ESPERADOS** em projetos Quarkus devido a:
CDI proxies (`*_ClientProxy`)
AOP enhancements (`*_Subclass`)
Panache bytecode enhancement
CDI wrappers (`*$$CDIWrapper`)

Eles **NÃO afetam** a execução dos testes ou a análise de cobertura real.

---

### 🔍 Opção 3: SonarQube (Análise Completa)
**Vantagens:**
- ✅ Code quality + Coverage + Security
- ✅ Histórico de métricas
- ✅ Quality gates
- ✅ Dashboards visuais

**Comando:**
```bash
mvn clean verify sonar:sonar `
  -Dsonar.projectKey=Painel-de-Investimentos `
  -Dsonar.projectName="Painel de Investimentos" `
  -Dsonar.host.url=http://localhost:9000 `
  -Dsonar.token=SEU_TOKEN_AQUI
```

**Dashboard:** http://localhost:9000/dashboard?id=Painel-de-Investimentos

---


## 🗄️ Exemplo de Configuração para SQL Server

Para produção, recomenda-se o uso de SQL Server. Exemplo de configuração no `application.yml`:
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

Para testes/desenvolvimento, pode-se usar H2 (in-memory).

### Métricas de Testes
**Total de Testes:** 187
**Sucessos:** 187 (100%)
- **Falhas:** 0
- **Cobertura Real (IntelliJ - Oficial):** **97,3%** ✅

### Cobertura Real por Módulo (IntelliJ Coverage)
| Pacote | Classes | Métodos | Branches | Linhas |
|--------|---------|---------|----------|--------|
| **Controllers** | 100% (5/5) | 100% (14/14) | 100% (2/2) | **100%** (67/67) ✅ |
| **Domain** | 100% (11/11) | 100% (19/19) | 90% (18/20) | **100%** (49/49) ✅ |
| **Security** | 100% (2/2) | 100% (6/6) | - | **100%** (19/19) ✅ |
| **Services** | 100% (1/1) | 100% (3/3) | 100% (6/6) | **100%** (10/10) ✅ |
| **Config** | 100% (1/1) | 100% (1/1) | - | **100%** (1/1) ✅ |
| **Application** | 0% (0/1) | 0% (0/3) | - | 0% (0/4) ⚠️ |


### Resumo Geral
- **Classes:** 95,2% (20/21)
- **Métodos:** 93,5% (43/46)
- **Branches:** 92,9% (26/28)
- **Linhas:** **97,3%** (146/150)

- ✅ Cobertura mínima: 97% (IntelliJ)
- ✅ Zero bugs críticos
- ✅ Zero vulnerabilidades de segurança
- ✅ Duplicação de código: <3%

---

## 🛠️ Configuração JaCoCo

### Exclusões Configuradas
```xml
<excludes>
    <exclude>**/generated/**</exclude>
    <exclude>**/*_ClientProxy*</exclude>      <!-- CDI proxies -->
    <exclude>**/*_Subclass*</exclude>         <!-- AOP subclasses -->
    <exclude>**/*$$CDIWrapper*</exclude>      <!-- CDI wrappers -->
    <exclude>**/*Test.class</exclude>         <!-- Classes de teste -->
    <exclude>**/*IntegrationTest.class</exclude>
    <exclude>**/*UnitTest.class</exclude>
    <exclude>**/*EnhancedTest.class</exclude>
    <exclude>**/*ValidationTest.class</exclude>
</excludes>
```

### Fases de Execução
1. **prepare-agent** (antes dos testes) - Instrumenta bytecode
2. **report** (fase verify) - Gera relatório HTML
3. **check** (fase verify) - Valida cobertura mínima (60%)

---


## 📝 Recomendações

**Dica:** Para usar SQL Server, instale o driver JDBC no `pom.xml`:
```xml
<dependency>
  <groupId>com.microsoft.sqlserver</groupId>
  <artifactId>mssql-jdbc</artifactId>
  <version>12.6.1.jre11</version>
</dependency>
```

### Para Desenvolvimento Local
👉 **Use IntelliJ IDEA Coverage** para análise rápida e precisa

### Para CI/CD Pipeline
👉 **Use Maven + JaCoCo + SonarQube** para análise automatizada

### Para Apresentações
👉 **Use SonarQube Dashboard** para métricas visuais profissionais

---

## 📚 Referências

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Quarkus Testing Guide](https://quarkus.io/guides/getting-started-testing)
- [SonarQube Java Coverage](https://docs.sonarsource.com/sonarqube/latest/analyzing-source-code/test-coverage/java-test-coverage/)
- [IntelliJ IDEA Coverage](https://www.jetbrains.com/help/idea/code-coverage.html)

---

**Última atualização:** 19/11/2025  
**Versão do projeto:** 1.0.0  
**Framework:** Quarkus 3.8.6  
**Java:** 21

**Nota:** Resultados do IntelliJ IDEA Coverage são a métrica oficial para documentação, compliance e apresentação.
