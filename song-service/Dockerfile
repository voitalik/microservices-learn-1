FROM maven:3.9.16-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/song-service-*.jar app.jar

EXPOSE 8072

CMD ["java", "-jar", "app.jar"]