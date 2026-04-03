# Pattern 04: Config Server with Spring Cloud Config

## Overview
Centralized configuration management allows all microservices to fetch their configuration from a central server. Changes can be made without redeploying services, and configurations can vary by environment (dev, staging, prod).

## Architecture

```
  ┌─────────────────────────────────────────────────────────┐
  │                   Config Server                          │
  │                 (localhost:8888)                         │
  │                                                         │
  │  Stores configs in:                                     │
  │  classpath:/config/config-client.yml                    │
  │  (or git repository)                                    │
  └──────────────────────┬──────────────────────────────────┘
                         │ fetch config on startup
                         ▼
  ┌─────────────────────────────────────────────────────────┐
  │                  Config Client                           │
  │                 (localhost:8080)                         │
  │                                                         │
  │  Properties injected from Config Server:               │
  │  app.message, app.version, feature.enabled             │
  └─────────────────────────────────────────────────────────┘
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| config-server | 8888 | Central configuration server |
| config-client | 8080 | Service that fetches config from server |

## How to Run

### Step 1: Start Config Server
```bash
cd config-server
mvn spring-boot:run
```

### Step 2: Start Config Client
```bash
cd config-client
mvn spring-boot:run
```

## Test Endpoints

```bash
# Get config from server directly
curl http://localhost:8888/config-client/default

# Get config values via client
curl http://localhost:8080/config

# Refresh config without restart (after changing config-client.yml)
curl -X POST http://localhost:8080/actuator/refresh
```

## Key Concepts
- **`@EnableConfigServer`**: Activates the config server
- **`spring.config.import=configserver:`**: Tells client to import from server
- **`@RefreshScope`**: Beans with this annotation reload on `/actuator/refresh`
- **Config Search Path**: Server searches for `{application}/{profile}` configs
