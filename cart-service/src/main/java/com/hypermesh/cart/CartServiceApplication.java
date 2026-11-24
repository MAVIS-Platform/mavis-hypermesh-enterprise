package com.hypermesh.cart;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class CartServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("cart-service", List.of("redis-session:6379"));
        CartServiceController controller = new CartServiceController();
        server.createContext("/api/v1/cart/preview", controller::previewCart);
        server.createContext("/api/v1/cart/validate", controller::validateCart);
        server.start();
        MeshRuntime.log("INFO", "cart-service", "Shopping Cart & Live Stock/Price Hydration ready and accepting traffic");
    }
}
