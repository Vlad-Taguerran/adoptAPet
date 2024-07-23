FROM  gradle:8.3.0-jdk17-focal AS build
COPY . /app
WORKDIR /app
RUN gradle build

FROM openjdk:17
LABEL authors="vinicius santana"
VOLUME /temp
COPY --from=build /app/build/libs/adotapet-0.0.1-SNAPSHOT.jar /libs/adotapet-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar", "./adopt"]