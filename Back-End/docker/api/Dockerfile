FROM eclipse-temurin:21-jdk-alpine

ENV PORT 8080
EXPOSE $PORT

COPY ./BackEndApplication.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]