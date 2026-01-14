# ───────────────────────────────────────────────────────────────────────────────
# Stage 1: Build with Maven + OpenJDK 23 (Eclipse Temurin 23 on Alpine)
# ───────────────────────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-23-alpine AS builder
WORKDIR /build

# 1) Copy just the pom.xml first, so that Maven can download dependencies and cache them.
COPY pom.xml .

# 2) Download all Maven dependencies (go-offline). This layer will be cached as long as pom.xml doesn't change.
RUN mvn dependency:go-offline -B

# 3) Copy the rest of your source code.
COPY src ./src

# 4) Package the application, skipping tests to speed up Docker builds.
RUN mvn clean package -DskipTests

# ───────────────────────────────────────────────────────────────────────────────
# Stage 2: Runtime image (only JRE 23) with “docker” profile activated
# ───────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Copy the “fat JAR” produced by Maven in the builder stage.
COPY --from=builder /build/target/*.jar app.jar

# Expose the port your Spring Boot app will listen on (default 8080).
EXPOSE 8080

# When the container starts, run:
#    java -jar app.jar --spring.profiles.active=docker
#
# That ensures Spring Boot picks up src/main/resources/application-docker.properties.
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
