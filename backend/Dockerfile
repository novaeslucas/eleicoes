FROM openjdk:8-jdk-alpine
MAINTAINER novaeslucas@gmail.com
COPY eleicoes.mv.db eleicoes.mv.db
COPY target/eleicoes-0.0.1-SNAPSHOT.jar eleicoes.jar
ENTRYPOINT ["java","-jar","/eleicoes.jar"]