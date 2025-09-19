# Multi-stage Dockerfile for Spring Boot 3.3 (Java 21)

# ====== Build stage ======
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

# Leverage Docker layer caching: first copy only descriptor files
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -e -DskipTests dependency:go-offline

# Then copy the full source and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -DskipTests package

# ====== Runtime stage ======
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /workspace/target/*.jar app.jar

# Render provides PORT; Spring is configured to read server.port from PORT
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UnlockExperimentalVMOptions -XX:+UseZGC"

# Expose for local use (Render injects PORT automatically)
EXPOSE 8080

# Use exec form to allow signals to reach the JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]


