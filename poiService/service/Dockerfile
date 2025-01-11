FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM eclipse-temurin:17 AS runtime
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/api/target/*-with-dependencies.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]