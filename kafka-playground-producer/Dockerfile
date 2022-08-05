FROM gradle:7.5.0-jdk17 AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk

EXPOSE 7081

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/producer.jar

ENTRYPOINT ["java","-jar","/app/producer.jar"]
