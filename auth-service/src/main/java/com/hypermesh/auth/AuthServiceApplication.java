package com.hypermesh.auth;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class AuthServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("auth-service", List.of("postgres-core:5432", "redis-session:6379"));
        AuthServiceController controller = new AuthServiceController();
        server.createContext("/api/v1/auth/verify", controller::verifyToken);
        server.createContext("/api/v1/auth/session", controller::sessionInfo);
        server.start();
        MeshRuntime.log("INFO", "auth-service", "Shared Identity, JWT & Session Verification ready and accepting traffic");
    }
}
