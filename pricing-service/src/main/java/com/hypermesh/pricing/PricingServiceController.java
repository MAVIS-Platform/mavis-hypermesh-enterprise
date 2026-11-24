package com.hypermesh.pricing;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Dynamic Pricing, Tax & Promotion Compute Engine */
@RestController
public class PricingServiceController {
    private static final String SERVICE_NAME = "pricing-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/pricing/quote")
    public void calculateQuote(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "calculateQuote", 16000, List.of());
    }
    @GetMapping("/api/v1/pricing/promotions")
    public void activePromotions(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "activePromotions", 16000, List.of());
    }
}
