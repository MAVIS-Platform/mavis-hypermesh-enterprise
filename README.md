# MAVIS HyperMesh Enterprise (16-Container Microservices Mesh)

Realistic, high-complexity 16-container enterprise e-commerce & FinTech microservices mesh built to stress-test **MAVIS Studio Live** at platform scale:

- **10 Instrumented Java 21 Microservices** (MAVIS Runner max 10/10):
  `api-gateway`, `auth-service`, `catalog-service`, `pricing-service`, `cart-service`, `order-service`, `inventory-service`, `payment-service`, `fraud-service`, `notification-worker`
- **6 Stateful Infrastructure Services**:
  `postgres-core`, `postgres-orders`, `postgres-ledger`, `redis-cache`, `redis-session`, `rabbitmq-broker`
- **18 Tangled Inter-Service HTTP Dependencies + 12 Infra Links** (depth-4 chains, diamond fan-out/fan-in, shared cross-cutting auth/pricing/inventory hubs).
- **100% OTel Trace Coverage**: `GET /api/v1/storefront/live-flow` traverses all 10 microservices end-to-end with real error propagation, real CPU work on `pricing-service`/`fraud-service`/`order-service`, and ultra-lean `<75MB` RAM footprint per JVM.
