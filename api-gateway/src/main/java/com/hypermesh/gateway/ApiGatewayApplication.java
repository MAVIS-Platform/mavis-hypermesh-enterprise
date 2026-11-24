package com.hypermesh.gateway;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class ApiGatewayApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("api-gateway", List.of());
        ApiGatewayController controller = new ApiGatewayController();
        server.createContext("/api/v1/storefront/live-flow", controller::storefrontLiveFlow);
        server.createContext("/api/v1/checkout/live-flow", controller::checkoutLiveFlow);
        server.createContext("/api/v1/catalog/browse", controller::browseCatalog);
        server.createContext("/api/v1/orders/overview", controller::ordersOverview);
        server.start();
        MeshRuntime.log("INFO", "api-gateway", "Edge API Gateway & Storefront BFF ready and accepting traffic");
    }
}
