# support-ticket-api

Ticket management API built with Spring Boot and Spring Data JPA.

## Features

- Create support tickets
- Read all support tickets
- Update existing tickets
- Delete existing tickets
- Persist ticket data with Spring Data JPA

## Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA

## API Endpoints

- `GET /tickets` - Return all tickets
- `POST /tickets` - Create a new ticket

Example request body for `POST /tickets`:

```json
{
  "title": "Cannot log in",
  "description": "User receives an invalid credentials error.",
  "status": "OPEN"
}
```

## Run Locally

1. Clone the repository
2. Start the application:

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.
