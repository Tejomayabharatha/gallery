# Step 1: Use Maven image to build the application
FROM maven:3.8.5-openjdk-17 AS build

# Step 2: Set the working directory for the build
WORKDIR /app

# Step 3: Copy the project files into the container
COPY . .

# Step 4: Build the project and create the JAR file
RUN mvn clean package

# Step 5: Use OpenJDK image for the final run
FROM openjdk:17-jdk-alpine

# Step 6: Set the working directory for the runtime
WORKDIR /app

# Step 7: Copy the JAR file from the build container
COPY --from=build /app/target/gallery-0.0.1-SNAPSHOT.jar app.jar

# Step 8: Expose the port (default 8080)
EXPOSE 8989

# Step 9: Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
