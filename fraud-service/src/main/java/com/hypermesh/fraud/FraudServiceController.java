package com.hypermesh.fraud;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Real-Time Transaction Risk & Fraud Scoring */
@RestController
public class FraudServiceController {
    private static final String SERVICE_NAME = "fraud-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/fraud/score")
    public void scoreTransaction(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "scoreTransaction", 12000, List.of());
    }
    @GetMapping("/api/v1/fraud/rules")
    public void riskRules(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "riskRules", 12000, List.of());
    }
}
