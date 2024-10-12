# Step 1: Use an official base image with Java 17
FROM openjdk:17-jdk-alpine

# Step 2: Set the working directory inside the Docker container
WORKDIR /app

# Step 3: Copy the JAR file into the container
# Replace 'sql-queries-0.0.1-SNAPSHOT.jar' with your actual JAR file name
COPY target/gallery-0.0.1-SNAPSHOT.jar

# Step 4: Expose the port that the Spring Boot application will run on (default is 8080)
EXPOSE 8080

# Step 5: Define the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
