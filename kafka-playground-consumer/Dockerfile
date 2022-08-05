FROM gradle:7.5.0-jdk17 AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk

EXPOSE 7080

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/consumer.jar

ENTRYPOINT ["java","-jar","/app/consumer.jar"]
