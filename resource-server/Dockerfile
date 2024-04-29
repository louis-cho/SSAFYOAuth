FROM azul/zulu-openjdk:21-latest

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
