# ---- Build Stage ----
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and pom first to leverage layer caching
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests; tests are run in CI before this stage)
RUN ./mvnw clean package -DskipTests -B

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

# Create non-root user for security
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

# Copy the fat JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Own the file by non-root user
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

