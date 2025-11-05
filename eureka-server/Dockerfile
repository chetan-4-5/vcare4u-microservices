# Use an official Maven image to build the project
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy all files to the container
COPY . .

# Package the application
RUN mvn clean package -DskipTests

# Use a lighter JDK image to run the jar
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar from the build stage
COPY --from=build /app/target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Expose Eureka server port
EXPOSE 8761

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
