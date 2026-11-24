package com.hypermesh.catalog;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Product Catalog & Merchandising */
@RestController
public class CatalogServiceController {
    private static final String SERVICE_NAME = "catalog-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/catalog/featured")
    public void featuredProducts(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "featuredProducts", 3500, List.of(new MeshRuntime.DownstreamCall("PRICING_SERVICE_URL", "http://pricing-service:8080", "/api/v1/pricing/quote"), new MeshRuntime.DownstreamCall("INVENTORY_SERVICE_URL", "http://inventory-service:8080", "/api/v1/inventory/stock")));
    }
    @GetMapping("/api/v1/catalog/items")
    public void listItems(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "listItems", 3500, List.of(new MeshRuntime.DownstreamCall("PRICING_SERVICE_URL", "http://pricing-service:8080", "/api/v1/pricing/quote")));
    }
}
