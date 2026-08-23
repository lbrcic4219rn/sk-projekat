FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY hotel-eureka-service/pom.xml hotel-eureka-service/pom.xml
COPY hotel-api-gateway-service/pom.xml hotel-api-gateway-service/pom.xml
COPY hotel-user-service/pom.xml hotel-user-service/pom.xml
COPY hotel-reservation-service/pom.xml hotel-reservation-service/pom.xml
COPY hotel-notification-service/pom.xml hotel-notification-service/pom.xml
RUN mvn -B -pl hotel-user-service -am dependency:go-offline

COPY hotel-user-service/src hotel-user-service/src
RUN mvn -B -pl hotel-user-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -r -u 1001 spring
COPY --from=build /build/hotel-user-service/target/hotel-user-service-0.0.1-SNAPSHOT.jar app.jar
USER spring
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
