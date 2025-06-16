FROM openjdk:21-jdk
#FROM openjdk:jdk-alpine

ARG ROOT_DIR
ENV ROOT_DIR $ROOT_DIR

WORKDIR /opt/app
COPY build/libs/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]