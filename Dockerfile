FROM gradle:8.14.2-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM amazoncorretto:17-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/wallet-application.jar
ENTRYPOINT ["java","-jar","/app/wallet-application.jar"]