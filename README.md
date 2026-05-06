# Spring Boot Controller Advice

A Spring Boot reference application demonstrating **centralized exception handling** using `@ControllerAdvice` and `@RestControllerAdvice`. The project also showcases Spring Security with role-based access control, a Thymeleaf-powered web UI, and a REST API documented with Swagger/OpenAPI.

---

## Table of Contents

- [What the Project Does](#what-the-project-does)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Run Locally](#run-locally)
  - [Run with Docker](#run-with-docker)
- [Default Credentials](#default-credentials)
- [Application URLs](#application-urls)
- [REST API Overview](#rest-api-overview)
- [Project Structure](#project-structure)
- [CI/CD](#cicd)
- [Contributing](#contributing)

---

## What the Project Does

This project is a practical example of how to implement **global exception handling** in a Spring Boot application through two dedicated handler classes:

| Handler | Annotation | Applies To | Response Format |
|---|---|---|---|
| `GlobalExceptionHandler` | `@ControllerAdvice` | MVC controllers (`/`) | Thymeleaf HTML error pages |
| `GlobalRestExceptionHandler` | `@RestControllerAdvice` | REST controllers (`/api/**`) | Structured JSON `ErrorResponse` |

Beyond exception handling, the application provides a fully functional user-management module secured by Spring Security, serving as a realistic playground for learning these patterns.

---

## Key Features

- ✅ **Centralized exception handling** — separate strategies for MVC and REST layers
- ✅ **Custom error pages** — user-friendly 403, 404, and 500 HTML error pages via Thymeleaf
- ✅ **Structured JSON error responses** — consistent `ErrorResponse` payload for REST consumers
- ✅ **Bean Validation integration** — `@Valid` request bodies with automatically formatted validation error messages
- ✅ **Role-based access control** — `ADMIN` and `USER` roles enforced by Spring Security
- ✅ **In-memory H2 database** — zero-configuration persistence for local development
- ✅ **Swagger UI** — interactive API documentation out of the box
- ✅ **Docker support** — multi-stage `Dockerfile` with a non-root runtime user
- ✅ **Jenkins CI/CD pipeline** — automated build, versioning, and Docker Hub push

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security 6 |
| Persistence | Spring Data JPA · H2 (in-memory) |
| Templating | Thymeleaf |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Mapping | MapStruct 1.6 |
| Boilerplate reduction | Lombok 1.18 |
| Build | Maven (Maven Wrapper) |
| Containerisation | Docker (Eclipse Temurin 21) |
| CI/CD | Jenkins |

---

## Getting Started

### Prerequisites

- **Java 21** (JDK)
- **Maven 3.9+** — or use the included `./mvnw` wrapper (no separate install needed)
- **Docker** *(optional, for containerised run)*

### Run Locally

```bash
# 1. Clone the repository
git clone https://github.com/prakasitnan/springboot-controller-advice.git
cd springboot-controller-advice

# 2. Build and start the application
./mvnw spring-boot:run
```

The application starts on **http://localhost:8080/springboot-controller-advice**.

> On Windows use `mvnw.cmd spring-boot:run` instead.

### Run with Docker

```bash
# Build the image
docker build -t springboot-controller-advice .

# Run the container
docker run -p 8080:8080 springboot-controller-advice
```

---

## Default Credentials

Two seed users are created automatically on first startup:

| Username | Password | Role |
|---|---|---|
| `admin` | `admin` | `ADMIN` |
| `user` | `user` | `USER` |

---

## Application URLs

| Path | Access | Description |
|---|---|---|
| `/springboot-controller-advice/login` | Public | Login page |
| `/springboot-controller-advice/home` | Authenticated | Home page |
| `/springboot-controller-advice/admin/dashboard` | `ADMIN` only | Admin dashboard |
| `/springboot-controller-advice/user/profile` | `USER` / `ADMIN` | User profile |
| `/springboot-controller-advice/user` | `USER` / `ADMIN` | User list |
| `/springboot-controller-advice/swagger-ui.html` | Authenticated | Swagger UI |
| `/springboot-controller-advice/h2-console` | Authenticated | H2 database console |

---

## REST API Overview

Base path: `/springboot-controller-advice/api/v1`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/user/all` | List all users |
| `GET` | `/user/{userId}` | Get a user by ID |
| `POST` | `/user` | Create a new user |
| `DELETE` | `/user/{userId}` | Soft-delete a user |

### Example — Create a user

```bash
curl -X POST http://localhost:8080/springboot-controller-advice/api/v1/user \
  -H "Content-Type: application/json" \
  -u admin:admin \
  -d '{
    "username": "john",
    "password": "secret",
    "name": "John Doe",
    "email": "john@example.com"
  }'
```

### Example — Error response (validation failure)

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "email: Email must be in the format mail@mail.com",
  "path": "/springboot-controller-advice/api/v1/user",
  "timestamp": "2026-04-29T10:00:00"
}
```

For the full interactive API reference, open [Swagger UI](http://localhost:8080/springboot-controller-advice/swagger-ui.html) after starting the application.

---

## Project Structure

```
src/main/java/.../springbootcontrolleradvice/
├── api/                      # REST controllers + GlobalRestExceptionHandler
│   ├── UserApi.java
│   └── GlobalRestExceptionHandler.java   # @RestControllerAdvice
├── controller/               # MVC controllers + GlobalExceptionHandler
│   ├── PageController.java
│   ├── UserController.java
│   └── GlobalExceptionHandler.java       # @ControllerAdvice
├── config/                   # Application configuration beans
├── dto/                      # Request / response DTOs & ErrorResponse
├── entity/                   # JPA entities (UserEntity)
├── exception/                # Custom exceptions (ResourceNotFoundException)
├── mapper/                   # MapStruct mappers
├── repository/               # Spring Data JPA repositories
├── security/                 # SecurityConfig + CustomUserDetailsService
├── service/                  # Business logic (UserService)
└── util/                     # Utility classes

src/main/resources/
├── application.properties    # App configuration
└── templates/
    ├── error/                # 403, 404, 500 error pages
    ├── fragments/            # Reusable Thymeleaf fragments
    └── pages/                # Feature pages (home, login, admin, user)
```

---

## CI/CD

The repository includes a **Jenkinsfile** that automates the following pipeline:

1. **Checkout** — pulls the `main` branch via SSH
2. **Update Version** — bumps `pom.xml` to `MAJOR.MINOR.BUILD_NUMBER`
3. **Build** — compiles the JAR and builds a Docker image
4. **Push to Docker Hub** — pushes versioned and `latest` tags to `prakasitnan/springboot-controller-advice`

Required Jenkins credentials:

| Credential ID | Type |
|---|---|
| `docker-hub-creds` | Username / Password |
| `git-repo-creds` | SSH private key |

---

## Contributing

1. Fork the repository and create a feature branch (`git checkout -b feature/my-change`)
2. Make your changes and ensure tests pass (`./mvnw test`)
3. Open a Pull Request against `main` with a clear description of what was changed and why

Bug reports and feature requests are welcome via [GitHub Issues](https://github.com/prakasitnan/springboot-controller-advice/issues).

---

*Maintained by [prakasitnan](https://github.com/prakasitnan)*

