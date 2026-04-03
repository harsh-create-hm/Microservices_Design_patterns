# Pattern 03: Circuit Breaker with Resilience4j

## Overview
The Circuit Breaker pattern prevents cascading failures in a distributed system. When a downstream service fails repeatedly, the circuit "opens" and subsequent calls fail fast instead of waiting, protecting system resources.

## Circuit States

```
    ┌──────────────────────────────────────────────────────┐
    │                                                      │
    │   CLOSED ──(failures exceed threshold)──► OPEN      │
    │     ▲                                      │        │
    │     │                                      │        │
    │     └──(success)── HALF-OPEN ◄──(timeout)──┘        │
    │                                                      │
    │  CLOSED:    Requests pass through normally           │
    │  OPEN:      Requests fail immediately (fast fail)    │
    │  HALF-OPEN: Limited requests to test recovery        │
    └──────────────────────────────────────────────────────┘
```

## Architecture

```
  Client ──► Payment Service ──► External Bank API
                    │                    │
                    │    Circuit Breaker │
                    │    monitors calls  │
                    │                   ▼
                    │         ┌─────────────────┐
                    │         │  CLOSED: pass   │
                    │         │  OPEN: fallback │
                    └────────►│  Fallback:      │
                              │  "Service       │
                              │   unavailable"  │
                              └─────────────────┘
```

## Service

| Service | Port | Description |
|---------|------|-------------|
| payment-service | 8080 | Payment processing with circuit breaker |

## How to Run

```bash
cd payment-service
mvn spring-boot:run
```

## Test Endpoints

```bash
# Process payment (calls external service with circuit breaker)
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{"orderId": "ORDER-001", "amount": 99.99, "currency": "USD"}'

# Get payment status
curl http://localhost:8080/payments/1

# Trigger circuit breaker by simulating failures
curl http://localhost:8080/payments/test-failure

# Check circuit breaker state via Actuator
curl http://localhost:8080/actuator/health
```

## Key Configuration
- `slidingWindowSize`: Number of calls tracked
- `failureRateThreshold`: Failure % to open circuit (50%)
- `waitDurationInOpenState`: How long circuit stays open (10s)
- `permittedNumberOfCallsInHalfOpenState`: Calls to test recovery (3)
