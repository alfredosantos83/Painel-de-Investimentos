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
RUN mvn clean package -DskipTests -Dquarkus.package.type=uber-jar

####
# Stage 2: Create the runtime image
####
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the uber-jar from build stage
COPY --from=build /app/target/*-runner.jar app.jar

# Create non-root user
RUN addgroup -S quarkus && adduser -S quarkus -G quarkus
USER quarkus

EXPOSE 8081

# Set JVM options for container
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
