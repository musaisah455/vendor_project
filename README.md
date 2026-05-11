# vendor_project

A **production-ready** Spring Boot REST API for managing vendors with JWT OAuth2 security, validation, DTOs, MapStruct mapping, and global exception handling.

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-4285F4?style=for-the-badge&logo=oauth&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-000000?style=for-the-badge)
![Keycloak](https://webvelocity.gr/wp-content/uploads/2023/01/kc.png)

## Table of Contents
- [Technologies & Features](#-technologies--features)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Database Setup](#-database-setup-postgresql)
- [Running the Application](#-running-the-application)
- [API Endpoints](#-api-endpoints)
- [Authentication](#-authentication)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)


- **Java 21** + **Spring Boot 3.3**
- **PostgreSQL** (Production) + H2 (Development)
- **OAuth2 Resource Server** with JWT Bearer Token
- **DTO Pattern** with Jakarta Bean Validation
- **MapStruct** for entity mapping
- Global Exception Handling
- Pagination & Search support
- RESTful API with proper HTTP status codes
- Production-ready structure (profiles, logging, actuator)

### Key Features

- Complete CRUD operations for Vendors
- Input validation with meaningful error messages
- Secure JWT authentication
- Responsive search and pagination
- Clean architecture (Layered + DTO + Mapper)

## Prerequisites

- **Java 21** or higher
- **Maven** 3.8+
- **PostgreSQL** 15+ (for production)
- Docker (optional but recommended)
- OAuth2 Authorization Server (Keycloak, Auth0, Spring Authorization Server, etc.)

## Installation & Setup

1. Clone the repository:
```bash
   git clone <your-repository-url>
   cd vendor-application

```

## Database Setup (PostgreSQL)

### Option 1: Docker (Recommended)

````
docker run --name vendor-postgres \
-e POSTGRES_DB=vendor_db \
-e POSTGRES_USER=vendor_user \
-e POSTGRES_PASSWORD=YourStrongPassword123! \
-p 5432:5432 \
-d postgres:16-alpine
````
### Option 2: Manual Setup

Run the following SQL script:

CREATE DATABASE vendor_db;

\c vendor_db

CREATE USER vendor_user WITH PASSWORD 'YourStrongPassword123!';

```bash

CREATE TABLE vendors (
id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
name            VARCHAR(100) NOT NULL,
email           VARCHAR(100) NOT NULL UNIQUE,
phone           VARCHAR(20),
address         VARCHAR(250),
status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
created_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
updated_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

#CONSTRAINT chk_vendor_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
);

CREATE INDEX idx_vendors_email ON vendors(email);
CREATE INDEX idx_vendors_name ON vendors(name);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE vendors TO vendor_user;
```

## Running the Application

### Development/Production (PostgreSQL)

```bash
./mvnw spring-boot:run
```

```bash
./mvnw spring-boot:run -Dspring.profiles.active=prod
```
Or using JAR:

````bash
java -jar target/vendor-application-1.0.0.jar
````
````bash
java -jar target/vendor-application-1.0.0.jar --spring.profiles.active=prod
````

## API Endpoints

| Method | Endpoint | Description                   | Request Body      
| :--- | :--- |:------------------------------|:------------------
|`POST` | `/api/v1/vendors` | `Create new vendor`           | `VendorRequestDto`
|`GET` | `/api/v1/vendors` | `Get all vendors (paginated)` | ______
|`GET` | `/api/v1/vendors/{id}` | `Get vendor by ID` | ______
|`PUT` | `/api/v1/vendors/{id}` | `Update vendor` | `VendorRequestDto`
|`DELETE` | `/api/v1/vendors/{id}` | `Delete vendor` | ______

### Query Parameters for GET All:

- search → Search by name (optional)
- page & size → Pagination support

### Example Request Body (Create/Update)
````bash
{
"name": "Acme Supplies Ltd",
"email": "contact@acme.example.com",
"phone": "+2348012345678",
"address": "Abuja Business District",
"status": "ACTIVE"
}
````

## Authentication
### This API is secured with OAuth2 JWT.

- Add header: Authorization: Bearer <your-jwt-token>
- Configure JWT_ISSUER_URI in application.yml or environment variable.
- Public endpoints: Swagger UI, Actuator Health

### Example with curl:

````bash
curl -H "Authorization: Bearer eyJhbGciOi..." http://localhost:8080/api/v1/vendors
````

## Project Structure

src/main/java/com/example/vendor/
- ├── config/
- ├── controller/
- ├── dto/
- ├── entity/
- ├── exception/
- ├── mapper/
- ├── repository/
- ├── service/
- &ensp;│   &ensp; &ensp; &ensp;├── impl/
- &ensp;│   &ensp; &ensp; &ensp;└── VendorService.java
- ├── VendorApplication.java
- └── ...

## Contributing
Contributions are welcome! Please fork the repository and create a pull request.

## License
This project is licensed under the MIT License.