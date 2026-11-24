package com.hypermesh.pricing;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class PricingServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("pricing-service", List.of("redis-cache:6379"));
        PricingServiceController controller = new PricingServiceController();
        server.createContext("/api/v1/pricing/quote", controller::calculateQuote);
        server.createContext("/api/v1/pricing/promotions", controller::activePromotions);
        server.start();
        MeshRuntime.log("INFO", "pricing-service", "Dynamic Pricing, Tax & Promotion Compute Engine ready and accepting traffic");
    }
}
