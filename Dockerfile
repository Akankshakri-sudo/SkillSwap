# ============================================================
# Stage 1: Build the application with Maven
# ============================================================
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper & pom.xml first to leverage Docker layer caching
COPY pom.xml .
COPY src ./src

# Build the executable JAR without running tests (tests already passed)
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

# ============================================================
# Stage 2: Minimal Production Runtime
# ============================================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Add a non-root system user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy built JAR from builder stage
COPY --from=build /app/target/skillswap-1.0.0.jar app.jar

# Cloud providers inject the PORT environment variable
ENV PORT=8080
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
