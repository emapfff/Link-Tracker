FROM maven:3.9.6-amazoncorretto-21-debian AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY .git /app/.git
COPY /scrapper/src/main /app/scrapper/src/main
COPY /scrapper/pom.xml /app/scrapper/pom.xml
RUN  mvn -f scrapper/pom.xml package

FROM openjdk:21-jdk-slim
EXPOSE 8080
COPY --from=build /app/scrapper/target/scrapper.jar /app/scrapper.jar

