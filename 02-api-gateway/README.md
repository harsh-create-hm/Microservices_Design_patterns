# Pattern 02: API Gateway with Spring Cloud Gateway

## Overview
The API Gateway pattern provides a single entry point for all client requests. It handles routing, load balancing, authentication, and cross-cutting concerns like logging and rate limiting.

## Architecture

```
                    ┌─────────────────────────────┐
                    │         API Gateway          │
                    │       (localhost:8080)        │
                    │                             │
  Client ──────────►│  Routes:                    │
                    │  /api/users/**  → user-svc  │
                    │  /api/products/**→ product-svc│
                    │                             │
                    │  Filters:                   │
                    │  - Logging Filter            │
                    │  - Request ID header         │
                    └──────┬───────────┬───────────┘
                           │           │
              ┌────────────▼───┐  ┌────▼──────────────┐
              │  User Service  │  │  Product Service   │
              │  (port 8081)   │  │  (port 8082)       │
              │  GET /users    │  │  GET /products     │
              └────────────────┘  └────────────────────┘
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| api-gateway | 8080 | API Gateway (Spring Cloud Gateway) |
| user-service | 8081 | User management |
| product-service | 8082 | Product catalog |

## How to Run

### Step 1: Start User Service
```bash
cd user-service
mvn spring-boot:run
```

### Step 2: Start Product Service
```bash
cd product-service
mvn spring-boot:run
```

### Step 3: Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

## Test Endpoints (all via gateway on port 8080)

```bash
# Get all users (routed to user-service:8081)
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/1

# Get all products (routed to product-service:8082)
curl http://localhost:8080/api/products

# Get product by ID
curl http://localhost:8080/api/products/1
```

## Key Concepts
- **Route**: Maps incoming path patterns to backend service URIs
- **Predicate**: Conditions for matching routes (path, method, header)
- **Filter**: Pre/post processing of requests (logging, auth, rate limiting)
- **`GatewayFilter`**: Applied to specific routes
- **`GlobalFilter`**: Applied to all routes
