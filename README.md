# Family Dinner Time

A Kotlin/Spring Boot backend for managing family dinners, dishes, ingredients, and steps, with JWT authentication and role-based access control.

## Features
- User registration, login, and JWT authentication
- Role-based access (admin, user)
- CRUD for dishes, ingredients, and steps
- Dishes are linked to their creator (user)
- Swagger UI for easy API exploration

## Requirements
- Java 17+
- Gradle
- PostgreSQL (see docker-compose for local setup)

## Getting Started

### 1. Clone the Repository
```sh
git clone <repo-url>
cd FamilyDinnerTime
```

### 2. Database Setup (Recommended: Docker)

Use the provided `docker-compose.yml` in the [dinner-bd](https://github.com/FamilyDinnerTime/dinner-bd) repository to start PostgreSQL and run migrations:
```sh
# Start DB and run migrations
# (requires Docker Compose)
docker-compose up
```

### 3. Configure Application

Edit `src/main/resources/application.yml` if you need to change DB connection settings.

### 4. Build and Run
```sh
./gradlew build
./gradlew bootRun
```

The app will start on [http://localhost:8080](http://localhost:8080)

## API Documentation

Once running, access the interactive API docs at:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Authentication
- Register and login to receive a JWT token.
- Pass the token as a `Bearer` token in the `Authorization` header for protected endpoints.

## Project Structure
- `src/main/kotlin/ru/kreslavski/family/dinnertime/controller` — REST controllers
- `src/main/kotlin/ru/kreslavski/family/dinnertime/service` — Business logic
- `src/main/kotlin/ru/kreslavski/family/dinnertime/repository` — JOOQ-based DB access
- `src/main/kotlin/ru/kreslavski/family/dinnertime/security` — JWT and security config
- `src/main/kotlin/ru/kreslavski/family/dinnertime/dto` — Request/response DTOs
- `src/main/resources` — Configuration files

## Useful Endpoints
- `/auth/register` — Register a new user
- `/auth/login` — Login and get JWT
- `/dishes` — CRUD for dishes (requires auth)
- `/ingredients` — CRUD for ingredients (admin only)
- `/steps` — CRUD for steps

## Development
- Code style: Kotlin, idiomatic, linter enabled
- API docs auto-generated with springdoc-openapi
- Tests: JUnit 5 (see `src/test`)

## License
MIT
