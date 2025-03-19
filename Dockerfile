
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/RatingSystem-0.0.1-SNAPSHOT.jar /app/RatingSystem-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/RatingSystem-0.0.1-SNAPSHOT.jar"]