# RHB Account Management System

## Overview

This project is a Spring Boot application for managing accounts, customers, and integration with external APIs. 
The application provides a REST API for account and customer operations, as well as an endpoint to fetch 
exchange rate data from an external API.

## Features

- CRUD operations for Accounts and Customers.
- Integration with an external API to fetch exchange rates.
- Pagination support for listing accounts and customers.
- Filter accounts and customers by various criteria.
- Logging requests and responses.

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- H2 in memory DB
- JUnit 5
- Maven

## Postman Collection
https://api.postman.com/collections/1547809-ff818bd5-f3f8-402a-9cd8-533a0b2831bf?access_key=PMAT-01J9K62K287207CFYGBVFXMFB4
## API Endpoints
### Customer Endpoints
- POST /customers - Create a new customer.

#### Request Example:
```shell
curl -X POST -H "Content-Type: application/json" -d '{"name":"Customer 1","email":"customer1@gmail.com"}' "http://localhost:8080/customers"
```

- GET /customers/search - Get all customers with pagination and optional filters (name, email).

#### Request Example:
```shell
curl -X GET "http://localhost:8080/customers/search?page=0&size=5"
```

- GET /customers/{id} - Get customer details by ID.

#### Request Example:
```shell
curl -X GET "http://localhost:8080/customers/1"
```

- PUT /customers/{id} - Update an existing customer.

#### Request Example:
```shell
curl -X PUT -H "Content-Type: application/json" -d '{"name":"Customer 2","email":"customer2@gmail.com"}' "http://localhost:8080/customers/1"
```

- DELETE /customers/{id} - Delete a customer by ID.

#### Request Example:
```shell
curl -X DELETE "http://localhost:8080/customers/1"
```

### Account Endpoints
- POST /accounts - Create a new account.

#### Request Example:
```shell
curl -X POST -H "Content-Type: application/json" -d '{"accountNumber":"1122334455","balance":3000.0,"customerId":1}' "http://localhost:8080/accounts"
```

- GET /accounts/search - Get all accounts with pagination and optional filters (account number, customer ID).

#### Request Example:
```shell
curl -X GET "http://localhost:8080/accounts/search?page=0&size=5"
```

- GET /accounts/{id} - Get account details by ID.

#### Request Example:
```shell
curl -X GET "http://localhost:8080/accounts/1"
```

- PUT /accounts/{id} - Update an existing account.

#### Request Example:
```shell
curl -X PUT -H "Content-Type: application/json" -d '{"accountNumber":"1111222233","balance":5000.0,"customerId":1}' "http://localhost:8080/accounts/1"
```

- DELETE /accounts/{id} - Delete an account by ID.

#### Request Example:
```shell
curl -X DELETE "http://localhost:8080/accounts/1"
```

### External API Endpoint
- GET /external-api/exchange-rate - Get exchange rate data from an external API.

#### Request Example:
```shell
curl -X GET "http://localhost:8080/external-api/exchange-rate?exchangePair=GBP_AUD"
```