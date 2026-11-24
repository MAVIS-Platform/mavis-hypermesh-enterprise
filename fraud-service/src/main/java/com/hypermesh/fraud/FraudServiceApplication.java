package com.hypermesh.fraud;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class FraudServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("fraud-service", List.of("postgres-ledger:5432", "redis-cache:6379"));
        FraudServiceController controller = new FraudServiceController();
        server.createContext("/api/v1/fraud/score", controller::scoreTransaction);
        server.createContext("/api/v1/fraud/rules", controller::riskRules);
        server.start();
        MeshRuntime.log("INFO", "fraud-service", "Real-Time Transaction Risk & Fraud Scoring ready and accepting traffic");
    }
}
