package com.hypermesh.gateway;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Edge API Gateway & Storefront BFF */
@RestController
public class ApiGatewayController {
    private static final String SERVICE_NAME = "api-gateway";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/storefront/live-flow")
    public void storefrontLiveFlow(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "storefrontLiveFlow", 1500, List.of(new MeshRuntime.DownstreamCall("AUTH_SERVICE_URL", "http://auth-service:8080", "/api/v1/auth/verify"), new MeshRuntime.DownstreamCall("CATALOG_SERVICE_URL", "http://catalog-service:8080", "/api/v1/catalog/featured"), new MeshRuntime.DownstreamCall("CART_SERVICE_URL", "http://cart-service:8080", "/api/v1/cart/preview"), new MeshRuntime.DownstreamCall("ORDER_SERVICE_URL", "http://order-service:8080", "/api/v1/orders/checkout-sim")));
    }
    @GetMapping("/api/v1/checkout/live-flow")
    public void checkoutLiveFlow(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "checkoutLiveFlow", 1500, List.of(new MeshRuntime.DownstreamCall("AUTH_SERVICE_URL", "http://auth-service:8080", "/api/v1/auth/verify"), new MeshRuntime.DownstreamCall("ORDER_SERVICE_URL", "http://order-service:8080", "/api/v1/orders/checkout-sim")));
    }
    @GetMapping("/api/v1/catalog/browse")
    public void browseCatalog(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "browseCatalog", 1500, List.of(new MeshRuntime.DownstreamCall("CATALOG_SERVICE_URL", "http://catalog-service:8080", "/api/v1/catalog/featured")));
    }
    @GetMapping("/api/v1/orders/overview")
    public void ordersOverview(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "ordersOverview", 1500, List.of(new MeshRuntime.DownstreamCall("AUTH_SERVICE_URL", "http://auth-service:8080", "/api/v1/auth/verify"), new MeshRuntime.DownstreamCall("ORDER_SERVICE_URL", "http://order-service:8080", "/api/v1/orders/summary")));
    }
}
