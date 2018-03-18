## Social Network Project - Friend Management

This module implements friend management of Social Network project.

### Installation and Getting Started

Please download the source code from.

```https://github.com/chenhaohowie/socialnetwork```

Go to the project folder and run below.

```docker-compose up```

It will download the image from the Docker Hub and bring up the application subsequently.

Or, you can run using maven.

```mvn clean package spring-boot:run```

Please access below URL once the application is up, which will facilitate the testing.

<http://localhost:8080/swagger-ui.html>

For accessing Database, please use below URL.

<http://localhost:8080/h2/login.jsp>

### Implementation details

1. The project is built on top of Spring Boot. Dependencies are Spring WEB + SPRING JPA + HIBERNATE + H2
2. Class introduction:
    * `FriendManagementController` class is the place where exposes REST APIs
    * `FriendManagementServiceImpl` class is the service layer
    * `PersonRepository` class is the data access layer
    * `Person` class is the self-contained entity that has associations with `FRIEND`, `SUBSCRIPTOR` and `BLOCKER`, which are all persons. 
    * And some other classes, like `GlobalExceptionHandler` which is the central place handling exceptions
3. Four tables will be created during application startup. `PERSON` table will be mapped to the entity and the rest are join tables as many-to-many relationship. Predefined data `data.sql` will be loaded into `PERSON` table.
4. To run the test cases only, use below
    * `mvn test`