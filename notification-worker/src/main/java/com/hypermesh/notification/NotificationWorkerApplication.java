package com.hypermesh.notification;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class NotificationWorkerApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("notification-worker", List.of("postgres-orders:5432", "rabbitmq-broker:5672"));
        NotificationWorkerController controller = new NotificationWorkerController();
        server.createContext("/api/v1/notifications/dispatch", controller::dispatchNotification);
        server.createContext("/api/v1/notifications/status", controller::workerStatus);
        server.start();
        MeshRuntime.log("INFO", "notification-worker", "Async Event Consumer & Webhook Dispatcher ready and accepting traffic");
    }
}
