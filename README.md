# User Info  Application

In this project, Spring Boot Rest Apis has been build using Spring Data JPA with H2 Database where user details has been taken, encrypted and stored in the database. 

## Overview
* Apis help to create users, retrieve encrypted users, retrieve decrypted user details.
* Each Users has id, title, firstName, lastName, email, dataOfBirth.
* Apis also performing encryption while storing user details into database.
* Provided dedicated Api to retrieve the decrypted user details.
* These are all Api's available in this project:
	1. POST /users
	2. GET /users
	3. GET /users/{id}
	4. GET /users/{id}/decrypt
* Making CRUD operations with Spring Data JPA’s JpaRepository.
* The database will be H2 Database (in memory DB).

## Technology Used
- Java 8
- Spring Boot 2.4 (with Spring Web MVC, Spring Data JPA)
- H2 Database
- Maven 3.6.1

## Project Structure
- User data model class corresponds to entity and table user.
- UserRepository is a JpaRepository to perform CRUD methods.
- UsersController is a RestController which has request mapping methods for RESTful requests such as: createUser, getAllUsers, getUserById, getDecryptedUserDetails
- Configuration for Spring Datasource, JPA & Hibernate in application.properties.
- pom.xml contains dependencies.

## Run & Test
Run Spring Boot application with command:

```
mvn spring-boot:run
```

## Access Database
users table will be automatically generated in Database once the application is started.

To access the database, open [H2-Console](http://localhost:8050/h2-console)
```
database=jdbc:h2:mem:testdb
username=admin
password=admin
```

## Postman Collection
Below postman collection can be used to test the APIs.
[https://www.getpostman.com/collections/50b34928a7912e607903](https://www.getpostman.com/collections/50b34928a7912e607903)

## Contributor
Saranya Vinayagam Battula
