# STAGE 1: Build the code using Maven and Java 26
FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Run the compiled code using Java 26 JRE
FROM eclipse-temurin:26-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# (Note: Keep this as EXPOSE 8081 in the kitchen-service!)
ENTRYPOINT ["java", "-jar", "app.jar"]