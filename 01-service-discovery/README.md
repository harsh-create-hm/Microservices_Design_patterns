# Pattern 01: Service Discovery with Eureka

## Overview
Service Discovery allows microservices to find and communicate with each other without hardcoded URLs. Netflix Eureka provides a service registry where services self-register and discover peers by name.

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Eureka Server                         │
│                   (localhost:8761)                       │
│                                                         │
│  Registry:                                              │
│  ┌─────────────────┐  ┌─────────────────┐              │
│  │  PRODUCT-SERVICE│  │  ORDER-SERVICE  │              │
│  │  192.168.1.1:8081│  │ 192.168.1.1:8082│              │
│  └─────────────────┘  └─────────────────┘              │
└──────────┬─────────────────────┬───────────────────────┘
           │ register            │ register
           ▼                     ▼
┌──────────────────┐   ┌──────────────────┐
│  Product Service │   │  Order Service   │
│  (port 8081)     │◄──│  (port 8082)     │
│                  │   │  calls product-  │
│  GET /products   │   │  service by name │
└──────────────────┘   └──────────────────┘
```

## How It Works
1. Eureka Server starts and listens for registrations
2. Product Service registers itself with its hostname and port
3. Order Service registers itself with its hostname and port
4. Order Service discovers Product Service by name (`PRODUCT-SERVICE`) via Eureka
5. Spring Cloud LoadBalancer resolves the name to an actual IP:port

## Services

| Service | Port | Description |
|---------|------|-------------|
| eureka-server | 8761 | Service registry |
| product-service | 8081 | Manages products |
| order-service | 8082 | Manages orders, calls product-service |

## How to Run

### Step 1: Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Open http://localhost:8761 to see the Eureka dashboard.

### Step 2: Start Product Service
```bash
cd product-service
mvn spring-boot:run
```

### Step 3: Start Order Service
```bash
cd order-service
mvn spring-boot:run
```

## Test Endpoints

```bash
# Get all products (directly)
curl http://localhost:8081/products

# Get a specific product
curl http://localhost:8081/products/1

# Place an order (order-service calls product-service via Eureka)
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'

# Get all orders
curl http://localhost:8082/orders

# Check registered services
curl http://localhost:8761/eureka/apps
```

## Key Concepts
- **Self-registration**: Services register themselves on startup via `@EnableDiscoveryClient`
- **Heartbeat**: Services send heartbeats every 30s to renew their lease
- **Eviction**: Services not sending heartbeats are evicted after 90s
- **`@LoadBalanced`**: Enables client-side load balancing on RestTemplate/WebClient
