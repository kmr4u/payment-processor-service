## Overview

The application processes payments using RESTful APIs. Payments have an id, and they can be approved or canceled through the provided 3rd Party Payment Processing API.

### Requirements

The service implements the following three User Stories:

1. As a User I want to create a payment so that I retrieve an approval code.
2. As a User I want to cancel an existing payment based on a given payment id.
3. As a User I want to search for a payment by a given payment id so that I can see the status of the payment.

# Prerequisites
* Java11, Docker are installed
* external-processor-api is running on port 8500. Use `docker-compose up external-processor-api` to run.
* MySQL server running on 3306 (MySQL service is added to docker-compose.yml. Use `docker-compose up db` to run the MySQL process)

# Application

The application is developed using Java11, SpringBoot and built using Gradle

The application can be launched locally by running `com.unzer.payments.PaymentProcessorApplication`. It will run on port 8080 by default

You can also run this via docker with the following commands:

```bash
docker-compose build payments
docker-compose up payments
```

Swagger documentation for the service end points can be found at: [/swagger endpoint](http://localhost:8080/swagger)

Postman collection for the service end points can be found [here](payments-service/docs/payments-service.postman_collection.json)

The application depends on the provided external processor API to approve payments. For more details about the 3rd Party Payment Processing API, see the README in ./external-processor-api: [here](external-processor-api/README.md)      

# Implementation

The service uses MySQL DB to store payments. 

It uses flyway migration scripts to manage creation/updation of DB tables.

It uses Spring Data JPA implementation to manage transactions with DB.

For Integration Testing the DB communication, the service uses `TestContainers` library in combination with SpringBoot-Starter-Test.

For interaction with external processor API, the service uses SpringBoot Web's `RestTemplate` for HTTP communication.

It uses `Slf4j` for logging and currently logs to console.

It uses `jacoco` plugin to generate test reports. To generate test report, run `gradle build jacocoTestReport` and the report can be found under `build/reports/jacoco`

# Future Improvements

The application can be improved to add Security - Authentication and Authorization. There are different ways we can handle this. One of the ways is we can use `Spring Security` to implement this.

The service currently logs to console. That can be improved to log to a central server so that we can have distributed tracing across all of our microservices.

The application can also include some metrics - Currently it uses `springboot-actuator` dependency and can report whatever comes out of the box from it.
