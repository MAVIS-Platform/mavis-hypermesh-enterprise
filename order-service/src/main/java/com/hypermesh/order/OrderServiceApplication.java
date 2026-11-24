package com.hypermesh.order;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class OrderServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("order-service", List.of("postgres-orders:5432", "rabbitmq-broker:5672"));
        OrderServiceController controller = new OrderServiceController();
        server.createContext("/api/v1/orders/checkout-sim", controller::checkoutSimulation);
        server.createContext("/api/v1/orders/summary", controller::orderSummary);
        server.start();
        MeshRuntime.log("INFO", "order-service", "Core Order Orchestrator & Saga Coordinator ready and accepting traffic");
    }
}
