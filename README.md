# Cards
> Allows users to create and manage tasks in the form of cards:.

## Important links

- [REST API docs](http://164.90.185.174:8080/swagger-ui/index.html#/)
- [api-docs](http://164.90.185.174:8080/api-docs)

## Getting started

Replace Application Properties values

cd into build/libs

Docker Image creation and run:

```
docker build -t cards .
docker run -d --restart unless-stopped --name card_cnt -p 8080:8080 cards
```
Insert Roles into the schema:
```
INSERT INTO roles(name) VALUES('ROLE_MEMBER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```

##  API Documentation

The API is documented using OpenAPI (Swagger).

## Copyright

Copyright 2023 Kogi. All Rights Reserved.
