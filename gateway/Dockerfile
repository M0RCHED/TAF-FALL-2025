FROM openjdk:21-jdk
ARG ROOT_DIR
ENV ROOT_DIR $ROOT_DIR

WORKDIR /opt/app
COPY  ./build/libs/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]