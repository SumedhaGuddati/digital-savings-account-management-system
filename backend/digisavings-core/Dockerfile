# Replace this
FROM eclipse-temurin:17-jdk

# With this (Amazon Corretto - more reliable)
FROM amazoncorretto:17

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]