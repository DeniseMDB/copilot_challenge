# Multi-stage Dockerfile for Excusas Sharks
# Build with Maven, run with a slim Java 17 runtime

# ---- Builder ----
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copy project files
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY src ./src

# Ensure mvnw is executable
RUN chmod +x mvnw || true

# Build the application (skip tests to speed up; remove -DskipTests to run tests in CI)
RUN ./mvnw -B clean package -DskipTests

# ---- Runner ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy jar from builder
ARG JAR_FILE=target/*-SNAPSHOT.jar
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]

