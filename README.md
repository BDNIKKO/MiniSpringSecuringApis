
# MiniSpringSecuringApis

This project is a Spring Boot application that implements JWT-based authentication and authorization for securing APIs. It demonstrates how to secure RESTful endpoints with user registration and login functionality, using JWT tokens to authenticate requests.

## Prerequisites

Before you can run this project, ensure you have the following:

- JDK 11 or later
- Maven
- Postman (optional, but helpful for testing the APIs)
- IntelliJ IDEA (or any other preferred IDE)

## Setup Instructions

### Step 1: Clone the repository
Clone the repository to your local machine using Git.

```bash
git clone <repository_url>
```

### Step 2: Navigate to the project directory
```bash
cd MiniSpringSecuringApis
```

### Step 3: Install dependencies
Use Maven to download and install all the dependencies listed in the `pom.xml`.

```bash
mvn clean install
```

### Step 4: Run the Spring Boot application
You can run the application using the Maven command below:

```bash
mvn spring-boot:run
```

Alternatively, you can run it within your IDE by navigating to the `MiniSpringSecuringApisApplication.java` file and running it as a Java application.

The application will start on port 8080 by default. If you need to change the port, modify the `application.properties` file.

## API Endpoints

### 1. User Registration (`/api/register`)

This endpoint allows users to register by providing a username and password.

- **Method**: `POST`
- **URL**: `http://localhost:8080/api/register`
- **Request Body** (JSON):
  ```json
  {
    "username": "your_username",
    "password": "your_password"
  }
  ```
- **Response**:
    - `200 OK` if registration is successful.
    - Example:
      ```json
      {
        "message": "User registered successfully",
        "data": "Success"
      }
      ```

### 2. User Login (`/api/login`)

This endpoint generates a JWT token for a registered user.

- **Method**: `POST`
- **URL**: `http://localhost:8080/api/login`
- **Request Body** (JSON):
  ```json
  {
    "username": "your_username",
    "password": "your_password"
  }
  ```
- **Response**:
    - `200 OK` if login is successful, with a JWT token.
    - Example:
      ```json
      {
        "message": "Login successful",
        "data": {
          "jwt": "your_jwt_token"
        }
      }
      ```

### 3. Access Secured Endpoint (e.g., `/api/protected`)

After logging in and obtaining the JWT token, you can access secured endpoints by including the JWT in the `Authorization` header.

- **Method**: `GET`
- **URL**: `http://localhost:8080/api/protected`
- **Headers**:
    - `Authorization: Bearer <your_jwt_token>`
- **Response**:
    - `200 OK` if the JWT token is valid.

### 4. H2 Database Console

The application uses an in-memory H2 database, accessible through the web console at:

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

## JWT Token Expiration

The JWT token expires in 1 hour (3600000ms). After expiration, you will need to log in again to get a new token.

## Security Configuration

JWT-based authentication has been implemented with the following configuration:
- Stateless session management.
- Public endpoints: `/api/register`, `/api/login`.
- All other endpoints require JWT token authentication.

## Running Tests

Unit tests are available in the `src/test/java` directory. Run the following command to execute the tests:

```bash
mvn test
```

## Troubleshooting

- **403 Forbidden**: Ensure you have a valid JWT token in the `Authorization` header when accessing secured endpoints.
- **Token Expired**: Re-log in to get a new JWT token if your token has expired.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- H2 In-Memory Database
- Maven
---
