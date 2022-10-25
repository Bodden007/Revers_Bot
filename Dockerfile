# syntax=docker/dockerfile:1
FROM openjdk:18.0.2.1-jdk-oracle
WORKDIR /Revers_Bot
COPY ./build/libs/Bot.jar /bot.jar
ENTRYPOINT ["java", "-jar", "/bot.jar"]