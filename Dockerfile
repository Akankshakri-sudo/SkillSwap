# ============================================================
# Stage 1: Build executable JAR using official Maven + JDK 21 image
# ============================================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build production JAR (skipping tests since they run during CI/local)
RUN mvn clean package -DskipTests

# ============================================================
# Stage 2: Minimal Production JRE Runtime
# ============================================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Run as non-root user
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy built JAR from Stage 1
COPY --from=build /app/target/skillswap-1.0.0.jar app.jar

# Cloud providers inject $PORT dynamically
ENV PORT=8080
EXPOSE 8080

# -XX:MaxRAMPercentage=75.0 and -XX:+UseSerialGC optimize JVM memory for 512MB cloud containers
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+UseSerialGC", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
