# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/webit-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will override with PORT env var)
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]