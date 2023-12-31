# Cards
> Allows users to create and manage tasks in the form of cards:.

## Important links

- [REST API docs](http://164.90.185.174:8080/swagger-ui/index.html#/)
- [api-docs](http://164.90.185.174:8080/api-docs)
- [POSTMAN API Collection](https://api.postman.com/collections/12721345-50a53212-73a3-4396-92d7-fd3458453124?access_key=PMAT-01H939M5AH4QYXNGD2RPK5TBPE)

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
Pulling Docker Image from Docker hub

```
docker pull erickogi14/kogi:latest
```


Insert Roles into the schema:
```
INSERT INTO roles(name) VALUES('ROLE_MEMBER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
Continue with other API Operations as listed on the swagger Doc

## HOSTED SERVICE API Documentation

The API is documented using OpenAPI (Swagger).
```
SignUp,SignIn,Create,List,Search,Get One, Patch, Delete
```

BASE URL.
```
http://164.90.185.174:8080/
```

[REST API docs](http://164.90.185.174:8080/swagger-ui/index.html#/)

TESTS
```
Unit Tests : Controllers & Services
Integration Tests : Cards Controller
```

## CopyRight

Copyright 2023 Kogi. All Rights Reserved.
