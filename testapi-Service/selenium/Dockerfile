FROM maven:3.8.6-eclipse-temurin-11 as builder

RUN apt-get update
# RUN apt-get install -y gconf-service libasound2 libatk1.0-0 libcairo2 libcups2 libfontconfig1 libgdk-pixbuf2.0-0 libgtk-3-0 libnspr4 libpango-1.0-0 libxss1 fonts-liberation libappindicator1 libnss3 lsb-release xdg-utils


RUN apt-get update && apt-get install -y wget gnupg curl unzip \
    && wget -qO google-chrome.deb "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb" \
    && dpkg -i google-chrome.deb || apt-get -fy install \
    && rm google-chrome.deb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /
COPY pom.xml .
COPY selenium ./selenium
WORKDIR /selenium
RUN mvn clean install
EXPOSE 8090
ENTRYPOINT ["mvn", "spring-boot:run" ]
