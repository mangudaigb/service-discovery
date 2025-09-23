FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY build/libs/agent-exchange-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]
