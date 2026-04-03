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

---

## Running & Testing in Spring Tool Suite (STS)

### 1. STS Setup

1. Download **STS 4** from https://spring.io/tools (supports Spring Boot 3.x).
2. Install **Java 17 JDK** and configure it in STS:
   `Window → Preferences → Java → Installed JREs → Add` → point to your JDK 17 home.
3. *(Optional)* Install the **Lombok** plugin:
   Run `java -jar lombok.jar` from the Lombok download and point it at your STS installation.

### 2. Importing a Pattern into STS

Each pattern folder is a **multi-module Maven project**.

1. `File → Import → Maven → Existing Maven Projects`
2. Browse to a pattern folder, e.g. `01-service-discovery/`
3. STS detects all `pom.xml` files in sub-folders automatically — check all of them.
4. Click **Finish**. STS downloads dependencies (first run may take a few minutes).
5. If you see red markers after import, right-click any project →
   `Maven → Update Project` (or press **Alt + F5**) → check **Force Update** → OK.

### 3. Starting Services in STS

#### Option A – Spring Boot Dashboard *(recommended)*
1. `Window → Show View → Other → Spring → Spring Boot Dashboard`
2. All detected Spring Boot apps appear in the list.
3. Right-click a service → **Start** (▶ green play button).
4. Watch the **Console** tab for `Tomcat started on port(s): XXXX`.

#### Option B – Run As
1. Open the main `*Application.java` class of the service.
2. Right-click → `Run As → Spring Boot App`.

#### Option C – Maven Goal
Right-click the service project → `Run As → Maven build…` → Goals: `spring-boot:run` → Run.

### 4. Startup Order per Pattern

Start services **in the order listed** — each row shows required startup sequence.

| Pattern | Start Order |
|---------|-------------|
| 01 Service Discovery | `eureka-server` → `product-service` → `order-service` |
| 02 API Gateway | `user-service` → `product-service` → `api-gateway` |
| 03 Circuit Breaker | `payment-service` |
| 04 Config Server | `config-server` → `config-client` |
| 05 Saga Pattern | `inventory-service` → `payment-service` → `order-service` |
| 06 CQRS | `product-service` |
| 07 Event Sourcing | `account-service` |
| 08 Bulkhead | `order-service` |
| 09 Distributed Tracing | `service-b` → `service-a` |
| 10 Load Balancer | `backend-service` ×2 instances → `client-service` |

### 5. Running Multiple Instances (Pattern 10 – Load Balancer)

To run two instances of `backend-service` on different ports in STS:

1. Right-click `backend-service` → `Run As → Run Configurations…`
2. Select **Spring Boot App** → click the **New** button (duplicate icon).
3. Name it `backend-service-8085`.
4. Go to the **Arguments** tab → **VM arguments**:
   ```
   -Dserver.port=8085
   ```
5. Click **Apply**, then **Run**.
6. Repeat steps 2–5, name it `backend-service-8092`, and set port `8092`.
7. Both instances appear in the Spring Boot Dashboard. Start `client-service` last.

### 6. Viewing Logs

- Each running service has its own **Console** tab.
- Use the **console dropdown** (monitor icon in the Console toolbar) to switch between service logs.
- Look for `Tomcat started on port(s): XXXX` to confirm a service started successfully.

### 7. H2 Database Console

Patterns 06 (CQRS) and 07 (Event Sourcing) use an in-memory H2 database.

- Pattern 06 (CQRS) URL: `http://localhost:8081/h2-console`
- Pattern 07 (Event Sourcing) URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa` | Password: *(leave blank)*

### 8. General STS Tips

| Tip | How |
|-----|-----|
| Hot reload during development | Add `spring-boot-devtools` to `pom.xml` or enable STS DevTools support |
| Fix dependency/classpath issues | Right-click project → `Maven → Update Project` (Alt+F5) |
| Check which port a service uses | Open `src/main/resources/application.yml` for that service |
| Stop a service | Click the red ■ **Stop** button in the Console or Spring Boot Dashboard |
| Re-run after a code change | Click the green ▶ button in the Spring Boot Dashboard |

---

## Postman Testing Scenarios

Import these requests into Postman (create a new **Collection** per pattern).  
Set `Content-Type: application/json` as a header for all POST/PUT requests.

---

### Pattern 01 – Service Discovery

> Start order: `eureka-server` (8761) → `product-service` (8081) → `order-service` (8082)

**1. Verify Eureka Dashboard**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8761` |
| Expected | Eureka dashboard HTML page listing registered services |

**2. Get all registered services (JSON)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8761/eureka/apps` |
| Headers | `Accept: application/json` |
| Expected | JSON listing `PRODUCT-SERVICE` and `ORDER-SERVICE` |

**3. Get all products (direct)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8081/products` |
| Expected | `200 OK` — JSON array of products |

**4. Get product by ID**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8081/products/1` |
| Expected | `200 OK` — single product JSON |

**5. Place an order (order-service resolves product-service via Eureka)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8082/orders` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"productId": 1, "quantity": 2}` |
| Expected | `200 OK` — order confirmation JSON |

**6. Get all orders**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8082/orders` |
| Expected | `200 OK` — JSON array containing the order created above |

---

### Pattern 02 – API Gateway

> Start order: `user-service` (8091) → `product-service` (8081) → `api-gateway` (8084)

**1. Get all users via gateway**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8084/api/users` |
| Expected | `200 OK` — JSON array of users (routed to user-service:8091) |

**2. Get user by ID via gateway**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8084/api/users/1` |
| Expected | `200 OK` — single user JSON |

**3. Get all products via gateway**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8084/api/products` |
| Expected | `200 OK` — JSON array of products (routed to product-service:8081) |

**4. Get product by ID via gateway**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8084/api/products/1` |
| Expected | `200 OK` — single product JSON |

**5. Verify custom request-ID header**

After sending any request above, check the **Response Headers** panel in Postman for an `X-Request-Id` header added by the gateway's global filter.

---

### Pattern 03 – Circuit Breaker

> Start: `payment-service` (8083)

**1. Process a payment (circuit CLOSED – normal flow)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8083/payments` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"orderId": "ORDER-001", "amount": 99.99, "currency": "USD"}` |
| Expected | `200 OK` — payment result JSON |

**2. Get payment by ID**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8083/payments/1` |
| Expected | `200 OK` — payment details JSON |

**3. Trigger repeated failures to open the circuit**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8083/payments/test-failure` |
| Expected | `200 OK` with a fallback message the first few times; after enough failures the circuit opens and subsequent calls immediately return the fallback without hitting the downstream |

**4. Check circuit breaker state**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8083/actuator/health` |
| Expected | `200 OK` — JSON showing `circuitBreakers` status (`CLOSED`, `OPEN`, or `HALF_OPEN`) |

> **Scenario tip:** Send request 3 six or more times rapidly in Postman using the **Runner** (Collections → Run). After `failureRateThreshold` is exceeded you will see the circuit flip to `OPEN` in the health endpoint.

---

### Pattern 04 – Config Server

> Start order: `config-server` (8888) → `config-client` (8087)

**1. Read raw config from Config Server**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8888/config-client/default` |
| Expected | `200 OK` — JSON containing `app.message`, `app.version`, and `feature.enabled` |

**2. Read resolved config values via client**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8087/config` |
| Expected | `200 OK` — JSON showing the properties injected from the config server |

**3. Hot-reload config without restart**

1. Edit `config-server/src/main/resources/config/config-client.yml` — change `app.message`.
2. Send the refresh trigger:

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8087/actuator/refresh` |
| Expected | `200 OK` — JSON array listing the changed property keys |

3. Re-send request 2 above and verify the updated value is returned.

---

### Pattern 05 – Saga Pattern

> Start order: `inventory-service` (8088) → `payment-service` (8083) → `order-service` (8082)

**1. Place an order (triggers full saga: payment + inventory reservation)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8082/orders` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"productId": 1, "quantity": 2, "customerId": "CUST-001", "amount": 199.98}` |
| Expected | `200 OK` — order JSON with status `CONFIRMED` |

**2. Get order status**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8082/orders/1` |
| Expected | `200 OK` — order with status `CONFIRMED` |

**3. Get all orders**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8082/orders` |
| Expected | `200 OK` — JSON array of orders |

**4. Check payment record**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8083/payments` |
| Expected | `200 OK` — payment record for the order above |

**5. Check inventory reservation**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8088/inventory/1` |
| Expected | `200 OK` — inventory record showing reduced stock |

**6. Saga failure scenario (insufficient stock)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8082/orders` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"productId": 1, "quantity": 9999, "customerId": "CUST-002", "amount": 99999.00}` |
| Expected | Order status should be `CANCELLED` and payment refunded (compensating transaction triggered) |

---

### Pattern 06 – CQRS

> Start: `product-service` (8081)

**1. Create a product (Command)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8081/commands/products` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"name": "Gaming Mouse", "description": "High DPI mouse", "price": 59.99, "stock": 100}` |
| Expected | `200 OK` — created product JSON with generated `id` |

**2. Update a product (Command)**

| Field | Value |
|-------|-------|
| Method | PUT |
| URL | `http://localhost:8081/commands/products/1` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"name": "Gaming Mouse Pro", "description": "Updated model", "price": 69.99, "stock": 80}` |
| Expected | `200 OK` — updated product JSON |

**3. Get all products (Query)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8081/queries/products` |
| Expected | `200 OK` — JSON array of all products |

**4. Get product by ID (Query)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8081/queries/products/1` |
| Expected | `200 OK` — single product JSON |

**5. Search products by name (Query)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8081/queries/products/search?name=Mouse` |
| Expected | `200 OK` — JSON array of matching products |

**6. Delete a product (Command)**

| Field | Value |
|-------|-------|
| Method | DELETE |
| URL | `http://localhost:8081/commands/products/1` |
| Expected | `200 OK` or `204 No Content` |

**7. Verify deletion (Query)**

Re-send request 4 (`GET /queries/products/1`) — expected `404 Not Found`.

**H2 Console:** Open `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, password: blank) to inspect the `PRODUCT` table directly.

---

### Pattern 07 – Event Sourcing

> Start: `account-service` (8080)

**1. Create a bank account**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8080/accounts` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"accountId": "ACC-001", "owner": "Alice", "initialBalance": 0}` |
| Expected | `200 OK` — account JSON with balance `0` |

**2. Deposit money (appends `AccountDeposited` event)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8080/accounts/ACC-001/deposit` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"amount": 1000.00}` |
| Expected | `200 OK` — updated account JSON with balance `1000.00` |

**3. Withdraw money (appends `AccountWithdrawn` event)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8080/accounts/ACC-001/withdraw` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"amount": 250.00}` |
| Expected | `200 OK` — updated account JSON with balance `750.00` |

**4. Get current account state (rebuilt by replaying all events)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8080/accounts/ACC-001` |
| Expected | `200 OK` — account JSON with balance `750.00` |

**5. Get full event history (audit log)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8080/accounts/ACC-001/events` |
| Expected | `200 OK` — JSON array with three events: `ACCOUNT_CREATED`, `MONEY_DEPOSITED`, `MONEY_WITHDRAWN` |

**H2 Console:** Open `http://localhost:8080/h2-console` to view the `ACCOUNT_EVENT` table and see the raw append-only event log.

---

### Pattern 08 – Bulkhead Pattern

> Start: `order-service` (8082)

**1. Process a single order (normal flow)**

| Field | Value |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8082/orders` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | `{"productId": 1, "quantity": 2, "customerId": "CUST-001"}` |
| Expected | `200 OK` — order JSON |

**2. Get all orders**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8082/orders` |
| Expected | `200 OK` — JSON array of orders |

**3. Check bulkhead health**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8082/actuator/health` |
| Expected | `200 OK` — JSON health showing bulkhead metrics |

**4. Trigger bulkhead saturation (Postman Runner)**

Use Postman's **Collection Runner** to send the POST `/orders` request 20 times with **0 ms delay** and **concurrent iterations**.

- First requests succeed (bulkhead slots available).
- Requests beyond `maxConcurrentCalls + maxWaitDuration` return a fallback response instead of failing entirely.
- The payment bulkhead and inventory bulkhead stay isolated from each other.

---

### Pattern 09 – Distributed Tracing

> Start order: `service-b` (8090) → `service-a` (8089)

**1. Call service-a (which internally calls service-b)**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8089/service-a/call` |
| Expected | `200 OK` — response from service-b, propagated back through service-a |

**2. Direct call to service-b**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8090/service-b/info` |
| Expected | `200 OK` — service-b info response |

**3. Verify trace propagation in STS Console**

After sending request 1, check the console logs of **both** services in STS. Each log line includes `traceId` and `spanId`:

```
INFO [service-a,traceId=abc123def456,spanId=aaa111] Calling service-b...
INFO [service-b,traceId=abc123def456,spanId=bbb222] Received request from service-a
```

Both services share the same `traceId` (`abc123def456`) — confirming end-to-end trace propagation via the `traceparent` HTTP header.

---

### Pattern 10 – Load Balancer

> Start order: `backend-service` on port 8085 → `backend-service` on port 8092 → `client-service` (8086)

**1. Load-balanced call through client-service**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8086/client/call` |
| Expected | `200 OK` — response from one of the backend instances (note the instance ID or port in the response body) |

**2. Repeat 4 more times and observe round-robin**

Send the same request 5 times (use Postman Runner). The response should alternate between `instance-8085` and `instance-8092`, confirming round-robin load balancing.

**3. Direct call to backend instance 1**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8085/backend/info` |
| Expected | `200 OK` — instance info showing port `8085` |

**4. Direct call to backend instance 2**

| Field | Value |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8092/backend/info` |
| Expected | `200 OK` — instance info showing port `8092` |
