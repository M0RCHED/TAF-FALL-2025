FROM maven:3.9.5-sapmachine-17 as builder

WORKDIR /code
COPY pom.xml .
COPY performance ./performance
COPY backend ./backend
COPY selenium ./selenium

WORKDIR /code/selenium
#RUN mvn clean install
RUN mvn clean package

#FROM maven:3.8.6-eclipse-temurin-11
FROM openjdk:17-slim

RUN apt-get update
# RUN apt-get install -y gconf-service libasound2 libatk1.0-0 libcairo2 libcups2 libfontconfig1 libgdk-pixbuf2.0-0 libgtk-3-0 libnspr4 libpango-1.0-0 libxss1 fonts-liberation libappindicator1 libnss3 lsb-release xdg-utils

RUN apt-get install -y wget
#RUN wget -q https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_114.0.5735.198-1_amd64.deb
RUN wget -q https://mirror.cs.uchicago.edu/google-chrome/pool/main/g/google-chrome-stable/google-chrome-stable_114.0.5735.198-1_amd64.deb
RUN apt-get install -y ./google-chrome-stable_114.0.5735.198-1_amd64.deb

WORKDIR /code

ARG JAR_FILE=target/selenium-1.0.0-SNAPSHOT.jar

COPY --from=builder /code/selenium/${JAR_FILE} /code/app.jar

EXPOSE 8090
ENTRYPOINT ["java","-jar","/code/app.jar"]

#ENTRYPOINT ["mvn", "spring-boot:run" ]
