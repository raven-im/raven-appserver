FROM openjdk:8-jdk-alpine
RUN mkdir -p /raven/
WORKDIR /raven/
COPY raven-appserver-1.0.0.jar /raven/raven-appserver.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","raven-appserver.jar"]