FROM maven:3.9.6-amazoncorretto-21-debian AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY .git /app/.git
COPY /bot/src/main/ /app/bot/src/main
COPY /bot/pom.xml /app/bot/pom.xml
COPY /models/src/main/ /app/models/src/main
COPY /models/pom.xml /app/models/pom.xml

RUN mvn -f /bot/pom.xml package

FROM openjdk:21-jdk-slim
EXPOSE 8090
COPY --from=build /app/bot/target/bot.jar /app/bot.jar

