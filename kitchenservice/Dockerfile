# Start with a lightweight Java environment
FROM eclipse-temurin:26-noble
# Create our working directory
WORKDIR /app

# Copy the compiled .jar file
COPY target/*.jar app.jar

# Expose port 8081
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]