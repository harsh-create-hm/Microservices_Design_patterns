# Pattern 05: Saga Pattern (Choreography-based)

## Overview
The Saga pattern manages distributed transactions across multiple microservices. In the choreography approach, each service publishes events that trigger the next step. If any step fails, compensating transactions roll back the previous steps.

## Saga Flow

```
  Order Service          Payment Service        Inventory Service
       │                       │                       │
       │  1. Create Order       │                       │
       │──────────────────────►│                       │
       │                       │  2. Process Payment   │
       │                       │──────────────────────►│
       │                       │                       │  3. Reserve Stock
       │                       │                       │──────────────────►
       │  4. Order Confirmed    │                       │
       │◄──────────────────────────────────────────────│
       │                       │                       │
       │  ─── FAILURE PATH ────────────────────────────│
       │                       │                       │
       │  Refund Payment        │                       │
       │──────────────────────►│                       │
       │  Release Stock         │                       │
       │──────────────────────────────────────────────►│
       │  Cancel Order          │                       │
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| order-service | 8080 | Orchestrates the saga |
| payment-service | 8081 | Processes payments, handles refunds |
| inventory-service | 8082 | Reserves/releases inventory |

## How to Run

### Start all services (in separate terminals):
```bash
cd inventory-service && mvn spring-boot:run
cd payment-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
```

## Test Endpoints

```bash
# Place an order (triggers the saga)
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2, "customerId": "CUST-001", "amount": 199.98}'

# Get order status
curl http://localhost:8080/orders/1

# Get all orders
curl http://localhost:8080/orders

# Check payment
curl http://localhost:8081/payments

# Check inventory
curl http://localhost:8082/inventory/1
```

## Key Concepts
- **Local Transaction**: Each service performs its own local database transaction
- **Compensating Transaction**: Undo operation if a later step fails
- **Event-driven**: Services communicate via events (simulated here with REST calls)
- **Eventual Consistency**: System reaches consistent state after all steps complete
