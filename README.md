# Cards
> Allows users to create and manage tasks in the form of cards:.

## Important links

- [REST API docs](http://164.90.185.174:8080/swagger-ui/index.html#/)
- [api-docs](http://164.90.185.174:8080/api-docs)

## Getting started

Replace Application Properties values
Build Jar
```
bootJar
```
Docker Image creation and run:

```
docker build -t cards .
docker run -d --restart unless-stopped --name card_cnt -p 8080:8080 cards
```

Docker file

```
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY cards_restful-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```


Insert Roles into the schema:
```
INSERT INTO roles(name) VALUES('ROLE_MEMBER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
Continue with other API Operations as listed on the swagger Doc

##  API Documentation

The API is documented using OpenAPI (Swagger).
[REST API docs](http://164.90.185.174:8080/swagger-ui/index.html#/)

## CopyRight

Copyright 2023 Kogi. All Rights Reserved.
