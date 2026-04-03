# Spring Boot Microservices Design Patterns

A comprehensive reference repository demonstrating 10 essential microservices design patterns using Spring Boot 3.x, Spring Cloud, and Java 17.

## Table of Contents

| # | Pattern | Description |
|---|---------|-------------|
| 01 | [Service Discovery](./01-service-discovery/) | Automatic service registration and discovery with Eureka |
| 02 | [API Gateway](./02-api-gateway/) | Single entry point for all client requests with routing & filtering |
| 03 | [Circuit Breaker](./03-circuit-breaker/) | Fault tolerance with Resilience4j |
| 04 | [Config Server](./04-config-server/) | Centralized configuration management |
| 05 | [Saga Pattern](./05-saga-pattern/) | Distributed transaction management via choreography |
| 06 | [CQRS Pattern](./06-cqrs-pattern/) | Command Query Responsibility Segregation |
| 07 | [Event Sourcing](./07-event-sourcing/) | Store state as a sequence of events |
| 08 | [Bulkhead Pattern](./08-bulkhead-pattern/) | Isolate failures with thread pool isolation |
| 09 | [Distributed Tracing](./09-distributed-tracing/) | Trace requests across services with Micrometer |
| 10 | [Load Balancer](./10-load-balancer/) | Client-side load balancing with Spring Cloud LoadBalancer |

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.x**
- **Spring Cloud 2023.0.x**
- **Maven**
- **Lombok**
- **H2 (in-memory database)**
- **Resilience4j**
- **Micrometer Tracing**

## Patterns Overview

### 1. Service Discovery
Enables services to find each other dynamically without hardcoded URLs. Netflix Eureka acts as a service registry where services register on startup and discover others by name.

**When to use:** When you have multiple microservices that need to communicate and their locations are dynamic (e.g., cloud environments, auto-scaling).

### 2. API Gateway
A single entry point that routes client requests to appropriate backend services. Handles cross-cutting concerns like authentication, rate limiting, and logging.

**When to use:** When you want to decouple clients from service topology, implement cross-cutting concerns centrally, or provide a unified API surface.

### 3. Circuit Breaker
Prevents cascading failures by "opening" the circuit when a service is failing, allowing it time to recover. Uses Resilience4j with CLOSED, OPEN, and HALF-OPEN states.

**When to use:** When calling external services or downstream dependencies that may fail, to prevent resource exhaustion and cascading failures.

### 4. Config Server
Centralize all configuration across services in one place. Services fetch their config at startup from the config server instead of bundling it locally.

**When to use:** When managing configuration across many services and environments (dev, staging, prod) and need to change config without redeployment.

### 5. Saga Pattern
Manages distributed transactions across multiple services using a sequence of local transactions coordinated via events (choreography) or a central orchestrator.

**When to use:** When a business transaction spans multiple services and you need eventual consistency without distributed ACID transactions.

### 6. CQRS (Command Query Responsibility Segregation)
Separates read (query) and write (command) operations into different models, allowing each to be optimized independently.

**When to use:** When read and write workloads have very different performance or scalability requirements, or when using event sourcing.

### 7. Event Sourcing
Instead of storing the current state, store a sequence of events that led to that state. Rebuild state by replaying events.

**When to use:** When you need a full audit trail, temporal queries, or want to rebuild state at any point in time.

### 8. Bulkhead Pattern
Isolates failures by partitioning system resources (thread pools) so that failure in one area doesn't affect others.

**When to use:** When calling multiple external services and you want to prevent one slow service from consuming all threads.

### 9. Distributed Tracing
Track requests as they flow across multiple services, providing end-to-end visibility with trace IDs and span IDs.

**When to use:** When debugging issues in a microservices architecture where requests span multiple services.

### 10. Load Balancer
Distribute incoming requests across multiple service instances to improve availability and throughput.

**When to use:** When running multiple instances of a service and need to distribute load, or for high availability.

## Getting Started

Each pattern directory is self-contained. Navigate to a pattern directory and follow its README for instructions.

### Prerequisites
- Java 17+
- Maven 3.8+

### Running a Pattern
```bash
cd 01-service-discovery
# Follow the README.md in that directory
```

## Project Structure

```
├── 01-service-discovery/    # Eureka Server + Client Services
├── 02-api-gateway/          # Spring Cloud Gateway
├── 03-circuit-breaker/      # Resilience4j Circuit Breaker
├── 04-config-server/        # Spring Cloud Config
├── 05-saga-pattern/         # Choreography Saga
├── 06-cqrs-pattern/         # CQRS with H2
├── 07-event-sourcing/       # Event Store pattern
├── 08-bulkhead-pattern/     # Resilience4j Bulkhead
├── 09-distributed-tracing/  # Micrometer Tracing
└── 10-load-balancer/        # Spring Cloud LoadBalancer
```
