package com.hypermesh.catalog;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class CatalogServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("catalog-service", List.of("postgres-core:5432", "redis-cache:6379"));
        CatalogServiceController controller = new CatalogServiceController();
        server.createContext("/api/v1/catalog/featured", controller::featuredProducts);
        server.createContext("/api/v1/catalog/items", controller::listItems);
        server.start();
        MeshRuntime.log("INFO", "catalog-service", "Product Catalog & Merchandising ready and accepting traffic");
    }
}
