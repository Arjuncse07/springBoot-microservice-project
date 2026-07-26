# springBoot-microservice-project

# Architecture

<img width="3096" height="3050" alt="main_diagram" src="https://github.com/user-attachments/assets/163a496c-090a-48b8-a1ee-cc29dab8ff67" />
                   

## Microservices

| Service                  | Port | Description                                        |
|--------------------------| ---- |----------------------------------------------------|
| **api-gateway**          | 8989 | Single entry point, path-based routing, Swagger UI |
| **catalog-service**      | 8081 | Product catalog (list, get by code)                |
| **order-service**        | 8082 | Order creation, validation, event publishing       |
| **notification-service** | 8083 | Listens to RabbitMQ order events                   |
| **user-service**         | 8085 | User registration, login, JWT authentication       |

## Infrastructure (Docker)

| Container             | Image                           | Host Port |
|-----------------------|---------------------------------| --------- |
| catalog-db            | postgres:16-alpine              | 15432     |
| orders-db             | postgres:16-alpine              | 25432     |
| notifications-db      | postgres:16-alpine              | 35432     |
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
- cd api-gateway && ./mvnw spring-boot:run

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
| Order → Catalog         | HTTP (sync)  | RestClient + Resilience4j retry/circuit breaker |
| Order → RabbitMQ        | AMQP (async) | Outbox pattern: DB → cron job → exchange        |
| RabbitMQ → Notification | AMQP (async) | `@RabbitListener` on `new-orders` queue         |


# Project Structure

<img width="3800" height="2150" alt="project_structure" src="https://github.com/user-attachments/assets/5a7af785-44a4-4a5a-8823-c3bb4b6a47d8" />




