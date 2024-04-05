FROM gradle:jdk17 as build_and_test

COPY . /app
WORKDIR /app

RUN ["gradle", "clean", "build", "test"]

FROM build_and_test as build_jar

WORKDIR /app

RUN ["gradle", "bootJar"]

FROM eclipse-temurin:17-jdk-alpine
COPY --from=build_jar /app/build/libs/dnn-back-0.0.1.jar /app/dnn-back.jar

ENTRYPOINT ["java", "-jar", "/app/dnn-back.jar"]
