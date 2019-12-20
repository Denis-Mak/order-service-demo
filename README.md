# Order REST API Demo

This is a demo of simple REST API based on Spring Boot. Angular UI app can be found in [order-front-demo](https://github.com/Denis-Mak/order-frontend-demo)  

## To run standalone in development mode

Build with maven `mvn clean package`

Run `java -jar target/order-service-demo-0.0.1-SNAPSHOT.jar` 

## To run in prod mode with Docker

Create Docker network for communication between container with Angular app
 and REST API container `docker network create demo-net`
 
Run container: `docker run --network demo-net --name api -p 8080:8080 -d denismakarskiy/order-service-demo`
