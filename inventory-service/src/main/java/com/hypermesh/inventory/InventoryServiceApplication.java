package com.hypermesh.inventory;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class InventoryServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("inventory-service", List.of("postgres-core:5432", "redis-cache:6379"));
        InventoryServiceController controller = new InventoryServiceController();
        server.createContext("/api/v1/inventory/stock", controller::checkStock);
        server.createContext("/api/v1/inventory/reserve", controller::reserveStock);
        server.start();
        MeshRuntime.log("INFO", "inventory-service", "Warehouse Stock Allocation & Reservation ready and accepting traffic");
    }
}
