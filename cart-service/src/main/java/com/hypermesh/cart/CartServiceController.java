package com.hypermesh.cart;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Shopping Cart & Live Stock/Price Hydration */
@RestController
public class CartServiceController {
    private static final String SERVICE_NAME = "cart-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/cart/preview")
    public void previewCart(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "previewCart", 3000, List.of(new MeshRuntime.DownstreamCall("PRICING_SERVICE_URL", "http://pricing-service:8080", "/api/v1/pricing/quote"), new MeshRuntime.DownstreamCall("INVENTORY_SERVICE_URL", "http://inventory-service:8080", "/api/v1/inventory/stock")));
    }
    @GetMapping("/api/v1/cart/validate")
    public void validateCart(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "validateCart", 3000, List.of(new MeshRuntime.DownstreamCall("CATALOG_SERVICE_URL", "http://catalog-service:8080", "/api/v1/catalog/items"), new MeshRuntime.DownstreamCall("INVENTORY_SERVICE_URL", "http://inventory-service:8080", "/api/v1/inventory/stock")));
    }
}
