package com.hypermesh.notification;

import com.hypermesh.common.MeshRuntime;
import com.hypermesh.common.RestAnnotations.GetMapping;
import com.hypermesh.common.RestAnnotations.RestController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/** Async Event Consumer & Webhook Dispatcher */
@RestController
public class NotificationWorkerController {
    private static final String SERVICE_NAME = "notification-worker";

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/api/v1/notifications/dispatch")
    public void dispatchNotification(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "dispatchNotification", 2000, List.of());
    }
    @GetMapping("/api/v1/notifications/status")
    public void workerStatus(HttpExchange exchange) throws IOException {
        MeshRuntime.handleRoute(exchange, SERVICE_NAME, "workerStatus", 2000, List.of());
    }
}
