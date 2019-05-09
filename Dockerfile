FROM openjdk:8-jdk-alpine
ARG JAR_FILE
RUN mkdir -p /raven/
WORKDIR /raven/
COPY ${JAR_FILE} /raven/raven-appserver.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","raven-appserver.jar"]