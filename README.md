# support-ticket-api

Ticket management API built with Spring Boot and Spring Data JPA.

## Features

- Create support tickets
- List all support tickets
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

1. Clone the repository.
2. Start PostgreSQL with Docker Compose:

```bash
docker compose up -d
```
3. Start the application:

```bash
./gradlew bootRun
```

4. Verify the API:

```bash
curl http://localhost:8080/tickets
```

The API will be available at `http://localhost:8080`.

## Quick Endpoint Test Commands

Create a ticket (`POST /tickets`):

```bash
curl -X POST http://localhost:8080/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Cannot log in",
    "description": "User receives an invalid credentials error.",
    "status": "OPEN"
  }'
```

List tickets (`GET /tickets`):

```bash
curl http://localhost:8080/tickets
```
