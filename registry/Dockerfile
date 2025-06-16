FROM alpine/java:21-jdk

RUN mkdir /app
WORKDIR /app
COPY build/libs/*.jar /app/eureka-server.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/eureka-server.jar", "--spring.profiles.active=docker"]
EXPOSE 8761