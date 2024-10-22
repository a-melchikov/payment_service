FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY ./bank_card /app/bank_card
COPY ./backend-commons /app/backend-commons

RUN mvn -f /app/backend-commons/pom.xml install -DskipTests
RUN mvn -f /app/bank_card/pom.xml package -DskipTests

FROM openjdk:17

COPY --from=build /app/bank_card/target/bank_card-1.0.0-SNAPSHOT.jar bank_card.jar

CMD ["java", "-jar", "bank_card.jar"]