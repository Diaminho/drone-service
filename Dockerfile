FROM openjdk:21-jdk-bullseye
COPY target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]