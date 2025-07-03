# Spring Boot Playground

This project is a demonstration and playground for building a modern, secure RESTful API using Spring Boot. It serves as a practical example of implementing common backend features, including stateless authentication with JWT, custom exception handling, and data persistence with JPA.

## A Note on Development: Prototyping with Gemini Code Assist

A significant portion of this project was developed and iterated upon using **Gemini Code Assist**. It was used as a pair-programming partner to:

*   **Rapidly Prototype:** Quickly generate boilerplate code for controllers, services, repositories, and security configurations.
*   **Implement Complex Features:** Assist in setting up Spring Security with JWT, including token generation, validation, and filter chains.
*   **Refactor and Improve Code:** Suggest improvements for code quality, efficiency, and best practices, such as refining JWT validation logic and implementing custom error responses.
*   **Generate Documentation:** Create Javadocs and this very README file to explain the project's structure and purpose.

This approach demonstrates how AI-powered coding assistants can significantly accelerate the development lifecycle, allowing developers to focus more on architecture and feature design rather than manual coding.

## Features

*   **RESTful API:** Endpoints for managing Users and Products (CRUD operations).
*   **Stateless Authentication:** Secure endpoints using JSON Web Tokens (JWT).
*   **Custom Security Handling:**
    *   `JwtAuthenticationFilter` to validate tokens on every request.
    *   `CustomAuthenticationEntryPoint` to provide structured JSON error responses for authentication failures.
*   **Data Persistence:** Uses Spring Data JPA with an in-memory H2 database.
*   **Validation:** Leverages `spring-boot-starter-validation` for request DTO validation.
*   **Global Exception Handling:** Centralized exception handler for clean and consistent error responses.

## Built With

*   Java 21
*   Spring Boot 3.x
*   Spring Security
*   Spring Data JPA
*   Maven
*   H2 Database
*   jjwt (Java JWT)

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   JDK 21 or later
*   Maven 3.6+

### Running the Application

1.  Clone the repository:
    ```sh
    git clone <repository-url>
    ```
2.  Navigate to the project directory:
    ```sh
    cd springboot-playground
    ```
3.  Run the application using the Maven wrapper:
    ```sh
    ./mvnw spring-boot:run
    ```
The application will start on `http://localhost:8080`.

## API Endpoints

Here are some of the main endpoints available:

### Authentication

*   `POST /api/auth/login`: Authenticate a user and receive a JWT.
    *   **Body:** `{ "username": "user", "password": "password" }`

### Users

*   `POST /api/users`: Create a new user (publicly accessible).
*   `GET /api/users`: Get a list of all users (requires authentication).
*   `GET /api/users/{id}`: Get a specific user by ID (requires authentication).
*   `PUT /api/users/{id}`: Update a user (requires authentication).
*   `DELETE /api/users/{id}`: Delete a user (requires authentication).

*(Note: A default user is not created. You must first use the `POST /api/users` endpoint to create a user before you can log in.)*