FROM openjdk:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

FROM openjdk:21-jdk
ARG ROOT_DIR
ENV ROOT_DIR $ROOT_DIR

WORKDIR /opt/app
COPY --from=builder /app/build/libs/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]