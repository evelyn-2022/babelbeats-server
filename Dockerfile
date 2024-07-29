# Use OpenJDK 22 base image
FROM openjdk:22-ea-jdk AS build

# Install Maven manually
RUN mkdir -p /usr/share/maven && \
    curl -sL https://archive.apache.org/dist/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz | tar -xz -C /usr/share/maven && \
    ln -s /usr/share/maven/apache-maven-3.8.5/bin/mvn /usr/bin/mvn

WORKDIR /app

# Copy Maven wrapper and pom files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Use a slimmer runtime base image
FROM openjdk:22-ea-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/babelbeats-server-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
