# Pattern 10: Load Balancer with Spring Cloud LoadBalancer

## Overview
Client-side load balancing distributes requests across multiple instances of a backend service. Spring Cloud LoadBalancer provides a modern, reactive alternative to Ribbon, supporting round-robin and random load balancing strategies.

## Architecture

```
                     ┌───────────────────────────────────────┐
  Client ──────────►│         Client Service                 │
                     │         (port 8080)                   │
                     │                                       │
                     │  Spring Cloud LoadBalancer            │
                     │  Round-robin across instances:        │
                     └──────┬─────────────────┬──────────────┘
                            │                 │
               ┌────────────▼──┐       ┌──────▼────────────┐
               │ Backend Svc   │       │ Backend Svc        │
               │ Instance 1    │       │ Instance 2         │
               │ (port 8081)   │       │ (port 8082)        │
               └───────────────┘       └────────────────────┘
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| client-service | 8080 | Makes load-balanced calls |
| backend-service (instance 1) | 8081 | Backend instance 1 |
| backend-service (instance 2) | 8082 | Backend instance 2 |

## How to Run

### Step 1: Start Backend Instance 1
```bash
cd backend-service
SERVER_PORT=8081 mvn spring-boot:run
```

### Step 2: Start Backend Instance 2
```bash
cd backend-service
SERVER_PORT=8082 mvn spring-boot:run
```

### Step 3: Start Client Service
```bash
cd client-service
mvn spring-boot:run
```

## Test Endpoints

```bash
# Make multiple calls and observe load balancing (notice alternating instance IDs)
curl http://localhost:8080/client/call
curl http://localhost:8080/client/call
curl http://localhost:8080/client/call

# Direct backend calls
curl http://localhost:8081/backend/info
curl http://localhost:8082/backend/info
```

## Key Concepts
- **Client-side LB**: Client decides which instance to call (vs server-side LB)
- **Round Robin**: Default strategy - each instance called in order
- **Service List**: Can be configured statically or discovered via Eureka
- **`@LoadBalanced`**: Enables load balancing on `RestTemplate`/`WebClient`
- **`ReactorLoadBalancer`**: Interface for custom load balancing algorithms
