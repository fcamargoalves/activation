FROM alpine/java:21

WORKDIR /app

COPY target/activation.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

