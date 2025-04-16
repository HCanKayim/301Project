FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/message-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"] 