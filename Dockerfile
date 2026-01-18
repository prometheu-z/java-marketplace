FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=build /app/target/app.jar app.jar
CMD ["java", "-jar", "app.jar"]