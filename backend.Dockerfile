FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY ./backend /app/backend
COPY ./backend-commons /app/backend-commons

RUN --mount=type=cache,target=/root/.m2 mvn -f /app/backend-commons/pom.xml install -DskipTests
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/backend/pom.xml package -DskipTests

FROM openjdk:17

COPY --from=build /app/backend/target/backend-1.0.0-SNAPSHOT.jar backend.jar

CMD ["java", "-jar", "backend.jar"]