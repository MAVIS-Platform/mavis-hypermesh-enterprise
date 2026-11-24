package com.hypermesh.payment;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Payment Authorization & Double-Entry Ledger */
@RestController
public class PaymentServiceController {
    private static final String SERVICE_NAME = "payment-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/payments/authorize")
    public void authorizePayment(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "authorizePayment", 4500, List.of(new MeshRuntime.DownstreamCall("AUTH_SERVICE_URL", "http://auth-service:8080", "/api/v1/auth/verify"), new MeshRuntime.DownstreamCall("FRAUD_SERVICE_URL", "http://fraud-service:8080", "/api/v1/fraud/score"), new MeshRuntime.DownstreamCall("NOTIFICATION_WORKER_URL", "http://notification-worker:8080", "/api/v1/notifications/dispatch")));
    }
    @GetMapping("/api/v1/payments/ledger")
    public void ledgerStatus(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "ledgerStatus", 4500, List.of());
    }
}
