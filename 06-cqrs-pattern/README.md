# Pattern 06: CQRS (Command Query Responsibility Segregation)

## Overview
CQRS separates write operations (Commands) from read operations (Queries). This allows each side to be optimized, scaled, and secured independently.

## Architecture

```
                    ┌─────────────────────────────────────┐
                    │           Product Service            │
                    │                                     │
  Write Request ───►│  Command Side                       │
  POST /products    │  ┌─────────────────────────────┐   │
  PUT /products/:id │  │ ProductCommandController    │   │
  DELETE /products  │  │ ProductCommandService       │   │
                    │  │ → validates & writes to DB  │   │
                    │  └─────────────────────────────┘   │
                    │                                     │
  Read Request  ───►│  Query Side                        │
  GET /products     │  ┌─────────────────────────────┐   │
  GET /products/:id │  │ ProductQueryController      │   │
                    │  │ ProductQueryService         │   │
                    │  │ → reads from DB (optimized) │   │
                    │  └─────────────────────────────┘   │
                    │                                     │
                    │       Shared: H2 Database           │
                    └─────────────────────────────────────┘
```

## Service

| Service | Port | Description |
|---------|------|-------------|
| product-service | 8080 | CQRS product management |

## How to Run

```bash
cd product-service
mvn spring-boot:run
```

## Test Endpoints

```bash
# COMMAND side - write operations
# Create a product
curl -X POST http://localhost:8080/commands/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Gaming Mouse", "description": "High DPI mouse", "price": 59.99, "stock": 100}'

# Update a product
curl -X PUT http://localhost:8080/commands/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Gaming Mouse Pro", "description": "Updated", "price": 69.99, "stock": 80}'

# Delete a product
curl -X DELETE http://localhost:8080/commands/products/1

# QUERY side - read operations
# Get all products
curl http://localhost:8080/queries/products

# Get product by ID
curl http://localhost:8080/queries/products/1

# Search products by name
curl http://localhost:8080/queries/products/search?name=Mouse
```

## Key Concepts
- **Command**: Mutation request (Create/Update/Delete) — never returns data, only success/failure
- **Query**: Read request — returns data, never changes state
- **Separation of Concerns**: Command side can use a write-optimized schema; Query side can use read-optimized projections
