FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/course-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"] 