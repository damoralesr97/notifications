# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compile and package the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the built artifact from the build stage
COPY --from=build /app/target/notifications-1.0-SNAPSHOT.jar app.jar

# Run the application using the classpath (since Manifest might no set Main-Class)
CMD ["java", "-cp", "app.jar", "com.morales.notifications.examples.MainExample"]
