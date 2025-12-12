# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom first to leverage cache
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copy source
COPY src ./src

# Build application (skip tests for faster image build)
RUN mvn -B -q clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/recetas-saludables-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
