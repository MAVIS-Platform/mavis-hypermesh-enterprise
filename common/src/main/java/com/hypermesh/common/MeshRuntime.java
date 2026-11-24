package com.hypermesh.common;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public final class MeshRuntime {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMillis(1200))
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private static final Instant STARTED_AT = Instant.now();
    private static final AtomicLong REQUEST_COUNTER = new AtomicLong();

    public record DownstreamCall(String envVar, String defaultBaseUrl, String path) {}

    public static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown-pod";
        }
    }

    public static void log(String level, String service, String message) {
        System.out.println(Instant.now() + " " + level + " [" + service + "@" + hostname() + "] " + message);
    }

    public static int intEnv(String key, int fallback) {
        String raw = System.getenv(key);
        if (raw == null || raw.isBlank()) return fallback;
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    public static String burnCpu(String seed, int iterations) {
        int loops = Math.max(100, iterations);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buf = seed.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < loops; i++) {
                buf = md.digest(buf);
            }
            StringBuilder hex = new StringBuilder(8);
            for (int i = 0; i < 4; i++) {
                hex.append(String.format("%02x", buf[i]));
            }
            return hex.toString();
        } catch (Exception e) {
            return "00000000";
        }
    }

    public static boolean probeTcp(String hostPort) {
        String[] parts = hostPort.split(":");
        if (parts.length != 2) return false;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(parts[0], Integer.parseInt(parts[1])), 600);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void handleRoute(
            HttpExchange exchange,
            String serviceName,
            String routeName,
            int defaultCpuWork,
            List<DownstreamCall> calls) throws IOException {
        long reqId = REQUEST_COUNTER.incrementAndGet();
        long startNs = System.nanoTime();
        int cpuWork = intEnv("CPU_WORK_ITERATIONS", defaultCpuWork);
        int extraLatencyMs = intEnv("ARTIFICIAL_LATENCY_MS", 0);
        if (extraLatencyMs > 0) {
            try { Thread.sleep(Math.min(extraLatencyMs, 5000)); } catch (InterruptedException ignored) {}
        }

        String digest = burnCpu(serviceName + ":" + routeName + ":" + reqId, cpuWork);
        List<String> okDeps = new ArrayList<>();
        List<String> failedDeps = new ArrayList<>();

        for (DownstreamCall call : calls) {
            String base = System.getenv(call.envVar());
            if (base == null || base.isBlank()) base = call.defaultBaseUrl();
            String url = base.endsWith("/") ? base.substring(0, base.length() - 1) + call.path() : base + call.path();
            try {
                HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                        .timeout(Duration.ofMillis(2200))
                        .header("X-HyperMesh-Caller", serviceName)
                        .GET()
                        .build();
                HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() >= 400) {
                    failedDeps.add(url + "->HTTP_" + resp.statusCode());
                    log("ERROR", serviceName, "Downstream error from " + url + " status=" + resp.statusCode());
                } else {
                    okDeps.add(url);
                }
            } catch (Exception ex) {
                String reason = ex.getClass().getSimpleName();
                failedDeps.add(url + "->" + reason);
                log("ERROR", serviceName, "Downstream call failed url=" + url + " reason=" + reason + " msg=" + ex.getMessage());
            }
        }

        long elapsedMs = (System.nanoTime() - startNs) / 1_000_000L;
        if (!failedDeps.isEmpty()) {
            String body = "{\"status\":\"DEGRADED\",\"service\":\"" + serviceName
                    + "\",\"route\":\"" + routeName
                    + "\",\"pod\":\"" + hostname()
                    + "\",\"elapsedMs\":" + elapsedMs
                    + ",\"failedDependencies\":\"" + String.join(", ", failedDeps).replace("\"", "'") + "\"}";
            respond(exchange, 503, body);
            return;
        }

        if (reqId % 10 == 1) {
            log("INFO", serviceName, "route=" + routeName + " req=" + reqId + " elapsedMs=" + elapsedMs + " deps=" + okDeps.size() + " checksum=" + digest);
        }
        String body = "{\"status\":\"OK\",\"service\":\"" + serviceName
                + "\",\"route\":\"" + routeName
                + "\",\"pod\":\"" + hostname()
                + "\",\"elapsedMs\":" + elapsedMs
                + ",\"checksum\":\"" + digest
                + "\",\"downstreamCount\":" + okDeps.size() + "}";
        respond(exchange, 200, body);
    }

    public static HttpServer createServer(String serviceName, List<String> infraTargets) throws IOException {
        int port = intEnv("SERVER_PORT", 8080);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 128);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.createContext("/health", exchange -> {
            long uptimeSec = Duration.between(STARTED_AT, Instant.now()).toSeconds();
            String body = "{\"status\":\"UP\",\"service\":\"" + serviceName
                    + "\",\"pod\":\"" + hostname()
                    + "\",\"uptimeSeconds\":" + uptimeSec
                    + ",\"requests\":" + REQUEST_COUNTER.get() + "}";
            respond(exchange, 200, body);
        });
        log("INFO", serviceName, "Starting on port " + port + " (infra targets=" + infraTargets.size() + ")");
        for (String target : infraTargets) {
            boolean reachable = probeTcp(target);
            log("INFO", serviceName, "Infra check " + target + " reachable=" + reachable);
        }
        return server;
    }

    public static void respond(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
