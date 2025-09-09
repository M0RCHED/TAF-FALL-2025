FROM maven:3.8.6-eclipse-temurin-11
WORKDIR /testapi
COPY pom.xml ./
COPY . .
RUN mvn clean install
EXPOSE 8082
ENTRYPOINT ["mvn", "spring-boot:run" ]