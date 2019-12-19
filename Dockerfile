FROM maven:3.6-jdk-8-slim AS build
COPY . /usr/src/app/
WORKDIR /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml clean package

FROM java:8-alpine
WORKDIR /
COPY --from=build /usr/src/app/target/*.jar /app.jar
CMD java -jar app.jar