# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

VOLUME /tmp
ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
