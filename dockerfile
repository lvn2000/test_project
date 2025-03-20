FROM sbt:1.10.11 AS build

WORKDIR /app
COPY . /app/

# Run tests and build the JAR
RUN sbt clean test assembly

# Stage 2: Create the runtime image
FROM openjdk:17-slim

WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/target/scala-2.13/test_project.jar /app/test_project.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/test_project.jar"]