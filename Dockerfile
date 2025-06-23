# Stage 1: build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace/app
COPY pom.xml .
COPY src ./src
RUN mvn -B package -DskipTests

# Stage 2: create a lean runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
