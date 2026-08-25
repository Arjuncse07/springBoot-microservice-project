# springBoot-microservice-project

# Architecture

                     ┌──────────────────────┐
                     │     API Gateway       │ :8989
                     │  (Spring Cloud GW)    │
                     └────┬────────────┬─────┘
                          │            │
              /catalog/** │            │ /orders/**
                          │            │
               ┌──────────▼──┐  ┌──────▼──────────┐
               │  Catalog    │  │   Order          │
               │  Service    │  │   Service        │
               │  :8081      │  │   :8082          │
               └──────┬──────┘  └──┬──────┬────────┘
                      │            │      │
                      │            │   RabbitMQ
                      │            │   :5672
               ┌──────▼──────┐     │  ┌───▼────────────┐
               │ catalog-db  │     │  │ Notification   │
               │ PostgreSQL  │     │  │ Service :8083  │
               │ :15432      │     │  └───┬────────────┘
               └─────────────┘     │      │
                           ┌───────▼──┐ ┌─▼──────────────┐
                           │orders-db │ │notifications-db│
                           │ :25432   │ │ :35432         │
                           └──────────┘ └────────────────┘





## Microservices

| Service                  | Port | Description                                        |
|--------------------------| ---- |----------------------------------------------------|
| **api-gateway**          | 8989 | Single entry point, path-based routing, Swagger UI |
| **catalog-service**      | 8081 | Product catalog (list, get by code)                |
| **order-service**        | 8082 | Order creation, validation, event publishing       |
| **notification-service** | 8083 | Listens to RabbitMQ order events                   |
| **library-service**      | 8084 | Library copies, loans, and access cards (planned)  |
| **user-service**         | 8085 | User registration, login, JWT authentication       |

## Infrastructure (Docker)

| Container             | Image                           | Host Port |
|-----------------------|---------------------------------| --------- |
| catalog-db            | postgres:16-alpine              | 15432     |
| orders-db             | postgres:16-alpine              | 25432     |
| notifications-db      | postgres:16-alpine              | 35432     |
| library-db              | postgres:16-alpine              | 46432     |
| user-db               | postgres:16-alpine              | 55432     |
| bookstore-rabbitmq    | rabbitmq:3.12.11-management     | 5672 / 15672 |

## Frontend

| App            | Port | Tech Stack                          |
| -------------- | ---- | ----------------------------------- |
| bookstore-ui   | 5173 | React, Vite, TypeScript, Tailwind CSS |

## Tech Stack

- **Java 21**, **Spring Boot 3.5**
- **Spring Cloud Gateway** (WebFlux)
- **Spring Data JPA** + **PostgreSQL**
- **Flyway** (database migrations)
- **RabbitMQ** (async messaging)
- **Resilience4j** (circuit breaker, retry)
- **Spring Security** + **JWT** (authentication)
- **SpringDoc OpenAPI** (Swagger UI)
- **Testcontainers** (integration testing)
- **Docker** (containerization via Spring Boot Buildpacks)
- **React 18** + **Vite** + **TypeScript** + **Tailwind CSS**

## Prerequisites

- Java 21
- Docker Desktop
- Node.js 18+ (for frontend)
- [Task](https://taskfile.dev/) (optional, for task runner)

## Getting Started

### 1. Start Infrastructure

## bash
- docker compose -f deployment/docker-compose/infra.yml up -d


## Run Backend Services
- cd catalog-service && ./mvnw spring-boot:run
- cd order-service && ./mvnw spring-boot:run
- cd library-service && ./mvnw spring-boot:run
- cd api-gateway && ./mvnw spring-boot:run

### Verify library-service (after infra + services are up)

```bash
curl http://localhost:8084/api/ping
curl http://localhost:8084/actuator/health
curl http://localhost:8989/library/api/ping
```

## Run Frontend 
- cd ../bookstore-ui
- npm install
- npm run dev
- 
## Inter-Service Communication

| From →        To        | Method       | Details                                         |
|-------------------------|--------------|-------------------------------------------------|
| Client → Gateway        | HTTP         | Path-based routing                              |
| Gateway → Catalog       | HTTP         | `/catalog/**` → `http://catalog-service:8081`   |
| Gateway → Orders        | HTTP         | `/orders/**` → `http://order-service:8082`      |
| Gateway → Library       | HTTP         | `/library/**` → `http://library-service:8084`  |
| Order → Catalog         | HTTP (sync)  | RestClient + Resilience4j retry/circuit breaker |
| Order → RabbitMQ        | AMQP (async) | Outbox pattern: DB → cron job → exchange        |
| RabbitMQ → Notification | AMQP (async) | `@RabbitListener` on `new-orders` queue         |


# Project Structure

springBoot-microservice-project-main/
├── api-gateway/              # Spring Cloud Gateway
├── catalog-service/          # Product catalog microservice
├── order-service/            # Order management microservice
├── notification-service/     # Event-driven notification service
├── library-service/          # Library copies, loans, access cards
├── user-service/             # User auth microservice (JWT)
├── deployment/
│   └── docker-compose/
│       ├── infra.yml         # PostgreSQL + RabbitMQ containers
│       └── apps.yml          # Application containers
├── .github/workflows/        # CI/CD pipelines
├── Taskfile.yml              # Task runner commands
└── pom.xml                   # Parent POM (multi-module)

