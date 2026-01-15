# Payment Service

This project is a RESTful API built with **Quarkus**, the Supersonic Subatomic Java Framework, designed to manage payment transactions. It uses PostgreSQL as the database and supports Docker for containerization.

## Tools and Technologies

The following tools and technologies were used to build this application:

- **Java 17**: Programming language.
- **Quarkus 3.30.6**: Java framework.
- **Maven**: Dependency management and build tool.
- **PostgreSQL**: Relational database.
- **Docker & Docker Compose**: Containerization and orchestration.
- **Hibernate ORM with Panache**: Simplified JPA operations.
- **Hibernate Validator**: Bean validation.
- **SmallRye OpenAPI**: API documentation (Swagger UI).

## Project Structure

The project follows a standard Maven/Quarkus directory structure:

```
src/main/java/org/acme/
├── domain/       # Domain entities and enums (e.g., Status, TypePayment)
├── dto/          # Data Transfer Objects (Request/Response records)
├── repository/   # Data access layer (Panache repositories)
├── resource/     # REST Controllers (API endpoints)
├── service/      # Business logic layer
└── exception/    # Custom exception handling
```

## Configuration (.env)

The application uses environment variables for configuration. A template is provided in `.env-example`.

1.  **Create the `.env` file**:
    Copy the example file to create your local configuration.
    ```bash
    cp .env-example .env
    ```

2.  **Configure variables**:
    Open `.env` and adjust the values if necessary.

    | Variable | Description | Default (Example) |
    | :--- | :--- | :--- |
    | `POSTGRES_DB` | Database name | `postgres` |
    | `POSTGRES_USER` | Database user | `postgres` |
    | `POSTGRES_PASSWORD` | Database password | `admin123` |
    | `DB_PORT` | Internal Database port | `5432` |
    | `APP_PORT` | Application port | `8080` |
    | `QUARKUS_DATASOURCE_URL` | JDBC URL for the app | `jdbc:postgresql://db:5432/postgres` |

    > **Note:** The `QUARKUS_DATASOURCE_URL` in the example is configured for the Docker network (hostname `db`). If running locally with `mvn quarkus:dev`, you may need to change the host to `localhost` and the port to the exposed port (e.g., `5433` as defined in docker-compose).

## How to Run

### Option 1: Using Docker Compose (Recommended)

This will start both the PostgreSQL database and the Quarkus application in containers.

1.  Ensure Docker and Docker Compose are installed.
2.  Set up your `.env` file as described above.
3.  Build and start the services:
    ```bash
    docker-compose up --build -d
    ```
4.  The application will be available at `http://localhost:8080`.
5.  The database will be exposed on port `5433` (as per `docker-compose.yml`).

### Option 2: Running Locally (Dev Mode)

1.  Start the database (you can use the db service from docker-compose):
    ```bash
    docker-compose up -d db
    ```
2.  Update your local configuration (or environment variables) to point to `localhost:5433`.
3.  Run the application in dev mode:
    ```bash
    ./mvnw quarkus:dev
    ```
    > **Dev UI:** Available at `http://localhost:8080/q/dev/`.

## API Endpoints

Base URL: `/api/payments`

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/api/payments` | Create a new payment. |
| **GET** | `/api/payments` | Retrieve a list of payments. Supports filtering. |
| **GET** | `/api/payments/{id}` | Retrieve a specific payment by ID. |
| **PATCH** | `/api/payments/{id}/status` | Update the status of a payment. |
| **DELETE** | `/api/payments/{id}` | Delete a payment. |

### Query Parameters for `GET /api/payments`

- `status`: Filter by payment status (e.g., PENDING, COMPLETED).
- `customerId`: Filter by customer ID.
- `from`: Start date-time filter.
- `to`: End date-time filter.
- `page`: Page number (default 0).
- `size`: Page size (default 10).

## API Examples

### Create Payment

**Request:**
`POST /api/payments`

```json
{
  "reference": "ORD-2023-001",
  "customerId": 101,
  "amount": 150.00,
  "currency": "USD",
  "method": "CARD"
}
```

**Response:**

```json
{
  "id": 1,
  "reference": "ORD-2023-001",
  "customerId": 101,
  "amount": 150.00,
  "currency": "USD",
  "method": "CARD",
  "status": "PENDING"
}
```

### Update Status

**Request:**
`PATCH /api/payments/1/status`

```json
{
  "status": "COMPLETED"
}
```

**Response:**

```json
{
  "id": 1,
  "reference": "ORD-2023-001",
  "customerId": 101,
  "amount": 150.00,
  "currency": "USD",
  "method": "CARD",
  "status": "COMPLETED"
}
```
