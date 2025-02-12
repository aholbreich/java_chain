FROM gradle:8.4-jdk21 AS builder

# Set the working directory
WORKDIR /app

# Copy the Gradle project files
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the fat JAR using the shadow plugin
RUN gradle shadowJar --no-daemon

# Stage 2: Create a lightweight runtime image
FROM eclipse-temurin:21-jre

# Copy the fat JAR from the builder stage
COPY --from=builder /app/build/libs/org.holbreich.java_chain-1.0.0.jar /app/app.jar

# Expose the application's port
EXPOSE 5000

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
