FROM openjdk:17
LABEL authors="vinicius santana"
VOLUME /temp
COPY build/libs/adotapet-0.0.1-SNAPSHOT.jar adopt
ENTRYPOINT ["java","-jar", "./adopt"]