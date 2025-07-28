# Stage 1: Build the application using Maven
# We use a specific Maven image that includes JDK 21 to match our project's Java version.
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project definition file
COPY pom.xml .

# Copy the rest of the application source code
COPY src ./src

# Run the Maven package command to build the application JAR file.
# The '-DskipTests' flag speeds up the build by not running tests.
RUN mvn clean package -DskipTests

# Stage 2: Create the final, lightweight container image
# We use a slim JRE image which is much smaller than a full JDK, making our final image more efficient.
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file that was built in the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080, which is the port our Spring Boot application runs on
EXPOSE 8080

# The command to run when the container starts
# This executes our Java application.
ENTRYPOINT ["java", "-jar", "app.jar"]
