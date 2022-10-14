FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/proxy-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8079
ENTRYPOINT ["java","-jar","app.jar"]