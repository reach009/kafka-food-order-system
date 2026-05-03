# 1. Start with a lightweight Linux server that has Java installed
# (I noticed you might be using a newer JDK in your logs. If you need a specific version, you can change '21' to your Java version)
FROM eclipse-temurin:26-noble

# 2. Create a folder inside the container called /app
WORKDIR /app

# 3. Copy the .jar file we just built from your Windows machine into the container
COPY target/*.jar app.jar

# 4. Expose port 8080 so the outside world can talk to it
EXPOSE 8080

# 5. Tell Docker the command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]