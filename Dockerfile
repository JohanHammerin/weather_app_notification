# Build stage
FROM gradle:8.10-jdk21 AS build
WORKDIR /app

# Copy gradle files first (for better caching)
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src ./src

# Build the application
RUN gradle clean build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]