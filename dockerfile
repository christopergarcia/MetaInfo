FROM openjdk:17-jdk-alpine

COPY target/metainfo-0.0.1-SNAPSHOT.jar metainfo-app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "metainfo-app.jar"]