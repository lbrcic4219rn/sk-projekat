FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY hotel-eureka-service/pom.xml hotel-eureka-service/pom.xml
COPY hotel-api-gateway-service/pom.xml hotel-api-gateway-service/pom.xml
COPY hotel-user-service/pom.xml hotel-user-service/pom.xml
COPY hotel-reservation-service/pom.xml hotel-reservation-service/pom.xml
COPY hotel-notification-service/pom.xml hotel-notification-service/pom.xml
RUN mvn -B -pl hotel-api-gateway-service -am dependency:go-offline

COPY hotel-api-gateway-service/src hotel-api-gateway-service/src
RUN mvn -B -pl hotel-api-gateway-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -r -u 1001 spring
COPY --from=build /build/hotel-api-gateway-service/target/hotel-api-gateway-service-0.0.1-SNAPSHOT.jar app.jar
USER spring
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
