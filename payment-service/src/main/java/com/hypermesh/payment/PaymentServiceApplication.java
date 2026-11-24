package com.hypermesh.payment;

import com.hypermesh.common.MeshRuntime;
import com.sun.net.httpserver.HttpServer;
import java.util.List;

public final class PaymentServiceApplication {
    public static void main(String[] args) throws Exception {
        HttpServer server = MeshRuntime.createServer("payment-service", List.of("postgres-ledger:5432", "rabbitmq-broker:5672"));
        PaymentServiceController controller = new PaymentServiceController();
        server.createContext("/api/v1/payments/authorize", controller::authorizePayment);
        server.createContext("/api/v1/payments/ledger", controller::ledgerStatus);
        server.start();
        MeshRuntime.log("INFO", "payment-service", "Payment Authorization & Double-Entry Ledger ready and accepting traffic");
    }
}
