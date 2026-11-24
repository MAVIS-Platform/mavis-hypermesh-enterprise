package com.hypermesh.order;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Core Order Orchestrator & Saga Coordinator */
@RestController
public class OrderServiceController {
    private static final String SERVICE_NAME = "order-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/orders/checkout-sim")
    public void checkoutSimulation(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "checkoutSimulation", 10000, List.of(new MeshRuntime.DownstreamCall("AUTH_SERVICE_URL", "http://auth-service:8080", "/api/v1/auth/verify"), new MeshRuntime.DownstreamCall("PRICING_SERVICE_URL", "http://pricing-service:8080", "/api/v1/pricing/quote"), new MeshRuntime.DownstreamCall("INVENTORY_SERVICE_URL", "http://inventory-service:8080", "/api/v1/inventory/reserve"), new MeshRuntime.DownstreamCall("PAYMENT_SERVICE_URL", "http://payment-service:8080", "/api/v1/payments/authorize"), new MeshRuntime.DownstreamCall("NOTIFICATION_WORKER_URL", "http://notification-worker:8080", "/api/v1/notifications/dispatch")));
    }
    @GetMapping("/api/v1/orders/summary")
    public void orderSummary(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "orderSummary", 10000, List.of(new MeshRuntime.DownstreamCall("INVENTORY_SERVICE_URL", "http://inventory-service:8080", "/api/v1/inventory/stock")));
    }
}
