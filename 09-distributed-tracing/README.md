# Pattern 09: Distributed Tracing with Micrometer

## Overview
Distributed tracing tracks requests as they propagate across multiple microservices. Each request gets a unique `traceId`, and each service hop creates a `spanId`. This enables end-to-end visibility and debugging across services.

## Architecture

```
  Client                Service A               Service B
    │                      │                       │
    │  GET /service-a/call  │                       │
    │─────────────────────►│                       │
    │                      │  traceId: abc123      │
    │                      │  spanId: span1        │
    │                      │                       │
    │                      │  GET /service-b/info  │
    │                      │──────────────────────►│
    │                      │  traceId: abc123      │
    │                      │  spanId: span2        │
    │                      │  parentSpanId: span1  │
    │                      │◄──────────────────────│
    │◄─────────────────────│                       │
    │  Response + traceId   │                       │
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| service-a | 8080 | Receives requests, calls service-b |
| service-b | 8081 | Downstream service |

## How to Run

```bash
cd service-b && mvn spring-boot:run
cd service-a && mvn spring-boot:run
```

## Test Endpoints

```bash
# Call service-a which internally calls service-b
curl http://localhost:8080/service-a/call

# Direct call to service-b
curl http://localhost:8081/service-b/info

# Check logs for traceId and spanId in both services
# Both services log: [traceId=abc123, spanId=span1]
```

## Key Concepts
- **Trace**: Entire journey of a request across all services (shared `traceId`)
- **Span**: Work done in a single service (`spanId`, `parentSpanId`)
- **Context Propagation**: `traceId` passed via HTTP headers (`traceparent`)
- **Micrometer Tracing**: Spring Boot 3.x tracing abstraction (replaces Sleuth)
- **Zipkin/Jaeger**: Visualization tools for collected traces
