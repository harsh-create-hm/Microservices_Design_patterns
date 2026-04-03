# Pattern 07: Event Sourcing

## Overview
Event Sourcing stores the state of an entity as a sequence of events rather than its current state. To get the current state, you replay all events from the beginning. This gives you a complete audit log and the ability to reconstruct state at any point in time.

## Architecture

```
  Client Request          Account Service           Event Store
       │                       │                         │
       │  deposit $100          │                         │
       │──────────────────────►│                         │
       │                       │  append AccountDeposited│
       │                       │────────────────────────►│
       │                       │                         │
       │  withdraw $30          │                         │
       │──────────────────────►│                         │
       │                       │  append AccountWithdrawn│
       │                       │────────────────────────►│
       │                       │                         │
       │  GET /accounts/1       │                         │
       │──────────────────────►│  load all events        │
       │                       │────────────────────────►│
       │                       │◄────────────────────────│
       │                       │  replay events:         │
       │                       │  balance = 0+100-30=$70 │
       │◄──────────────────────│                         │
```

## Service

| Service | Port | Description |
|---------|------|-------------|
| account-service | 8080 | Bank account with event sourcing |

## How to Run

```bash
cd account-service
mvn spring-boot:run
```

## Test Endpoints

```bash
# Create an account
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountId": "ACC-001", "owner": "Alice", "initialBalance": 0}'

# Deposit money (creates an event)
curl -X POST http://localhost:8080/accounts/ACC-001/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount": 1000.00}'

# Withdraw money (creates an event)
curl -X POST http://localhost:8080/accounts/ACC-001/withdraw \
  -H "Content-Type: application/json" \
  -d '{"amount": 250.00}'

# Get current state (rebuilt from events)
curl http://localhost:8080/accounts/ACC-001

# Get full event history (audit log)
curl http://localhost:8080/accounts/ACC-001/events
```

## Key Concepts
- **Event**: An immutable record of something that happened (e.g., `MoneyDeposited`)
- **Event Store**: Append-only log of all events
- **Aggregate**: Entity rebuilt by replaying its events
- **Projection**: Read model built from events (current state)
- **Event Replay**: Reconstruct state at any point by replaying events up to that time
