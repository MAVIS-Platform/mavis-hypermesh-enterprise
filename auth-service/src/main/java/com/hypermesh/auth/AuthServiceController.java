package com.hypermesh.auth;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Shared Identity, JWT & Session Verification */
@RestController
public class AuthServiceController {
    private static final String SERVICE_NAME = "auth-service";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/auth/verify")
    public void verifyToken(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "verifyToken", 2500, List.of());
    }
    @GetMapping("/api/v1/auth/session")
    public void sessionInfo(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "sessionInfo", 2500, List.of());
    }
}
