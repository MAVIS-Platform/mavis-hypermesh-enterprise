package com.hypermesh.inventory;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Warehouse Stock Allocation & Reservation */
@RestController
public class InventoryServiceController {
    private static final String SERVICE_NAME = "inventory-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/inventory/stock")
    public void checkStock(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "checkStock", 3000, List.of());
    }
    @GetMapping("/api/v1/inventory/reserve")
    public void reserveStock(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "reserveStock", 3000, List.of());
    }
}
