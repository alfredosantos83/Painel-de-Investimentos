####
# Stage 1: Build the application
####
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
# O formato 'fast-jar' é o padrão do Quarkus e otimizado para containers
RUN mvn package -DskipTests

####
# Stage 2: Create the runtime image
####
FROM eclipse-temurin:21-jre-alpine

# Create non-root user
RUN addgroup -S quarkus && adduser -S quarkus -G quarkus

WORKDIR /app

# Copia as dependências (camada que raramente muda)
COPY --from=build --chown=quarkus:quarkus /app/target/quarkus-app/lib/ /app/lib/
# Copia o código da aplicação (camada que muda com frequência)
COPY --from=build --chown=quarkus:quarkus /app/target/quarkus-app/*.jar /app/
COPY --from=build --chown=quarkus:quarkus /app/target/quarkus-app/app/ /app/app/
COPY --from=build --chown=quarkus:quarkus /app/target/quarkus-app/quarkus/ /app/quarkus/

EXPOSE 8081
USER quarkus

# Set JVM options for container
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport"

# Usa o formato 'exec' para que o processo Java seja o PID 1 e receba sinais do sistema
ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
