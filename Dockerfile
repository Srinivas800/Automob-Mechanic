# ─────────────────────────────────────────────────────────────
# Stage 1: BUILD
# Uses full JDK to compile the project
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy pom.xml first — Docker caches this layer
# If only source code changes, dependencies are NOT re-downloaded
COPY pom.xml .
RUN apk add --no-cache maven && mvn dependency:go-offline -q

# Now copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ─────────────────────────────────────────────────────────────
# Stage 2: RUN
# Uses tiny JRE only (no compiler, no Maven = smaller image)
# Result: ~180MB image instead of ~600MB single-stage image
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy only the built JAR from stage 1
COPY --from=build /app/target/*.jar app.jar

# JVM tuning for container environment:
# -XX:+UseContainerSupport     → detects container memory limits correctly
# -XX:MaxRAMPercentage=75.0    → uses 75% of container RAM (not all of it)
# -XX:+UseG1GC                 → G1 Garbage Collector (better for web apps)
# -Djava.security.egd=...      → faster startup (avoids entropy blocking)
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

EXPOSE 8080
