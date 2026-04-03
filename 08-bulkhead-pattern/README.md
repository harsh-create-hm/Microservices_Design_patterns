# Pattern 08: Bulkhead Pattern with Resilience4j

## Overview
The Bulkhead pattern isolates failures by partitioning system resources (thread pools, semaphores) so that a failure or slowdown in one area doesn't exhaust resources needed by other areas.

## Architecture

```
  ┌─────────────────────────────────────────────────────────┐
  │                    Order Service                         │
  │                                                         │
  │  ┌─────────────────────┐  ┌─────────────────────────┐  │
  │  │   Payment Bulkhead  │  │  Inventory Bulkhead      │  │
  │  │   Thread Pool: 5    │  │  Thread Pool: 5          │  │
  │  │   Queue: 10         │  │  Queue: 10               │  │
  │  │                     │  │                          │  │
  │  │  If payment service │  │  Even if payment is      │  │
  │  │  is slow/down,      │  │  overwhelmed, inventory  │  │
  │  │  only 5+10 threads  │  │  calls still work        │  │
  │  │  are affected       │  │                          │  │
  │  └─────────────────────┘  └─────────────────────────┘  │
  └─────────────────────────────────────────────────────────┘
```

## Service

| Service | Port | Description |
|---------|------|-------------|
| order-service | 8080 | Order service with bulkhead isolation |

## How to Run

```bash
cd order-service
mvn spring-boot:run
```

## Test Endpoints

```bash
# Process an order (uses bulkheads for payment and inventory calls)
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2, "customerId": "CUST-001"}'

# Get all orders
curl http://localhost:8080/orders

# Check bulkhead health
curl http://localhost:8080/actuator/health

# Simulate load to trigger bulkhead
for i in {1..20}; do
  curl -s -X POST http://localhost:8080/orders \
    -H "Content-Type: application/json" \
    -d "{\"productId\": $i, \"quantity\": 1, \"customerId\": \"CUST-$i\"}" &
done
```

## Key Concepts
- **Thread Pool Bulkhead**: Separate thread pools per integration point
- **Semaphore Bulkhead**: Limit concurrent calls using semaphores
- **`maxConcurrentCalls`**: Max simultaneous calls allowed
- **`maxWaitDuration`**: How long to wait for a bulkhead slot
- **Fallback**: Called when bulkhead is full
