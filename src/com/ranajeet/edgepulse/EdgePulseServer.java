package com.ranajeet.edgepulse;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class EdgePulseServer {

    public static void main(String[] args) throws Exception {
        int port = 5050;

        ExecutorService pool =
                Executors.newFixedThreadPool(8);

        CsvTelemetryLogger logger =
                new CsvTelemetryLogger();

        AlertEngine alerts = new AlertEngine();

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println(
                    "EdgePulse server listening on port " + port);

            while (true) {
                Socket client = server.accept();

                System.out.println(
                        "Connected: " + client.getRemoteSocketAddress());

                pool.submit(() -> handleClient(
                        client, logger, alerts));
            }
        }
    }

    private static void handleClient(
            Socket client,
            CsvTelemetryLogger logger,
            AlertEngine alerts) {

        try (Socket socket = client;
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream(),
                             StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    TelemetryRecord data =
                            TelemetryRecord.parse(line);

                    logger.log(data);
                    alerts.evaluate(data);

                    System.out.printf(
                            "[RECEIVED] %s | CPU %.1f%% | Memory %.1f%%"
                                    + " | Latency %.1f ms%n",
                            data.deviceId(),
                            data.cpu(),
                            data.memory(),
                            data.latency());

                } catch (IllegalArgumentException e) {
                    System.err.println(
                            "[INVALID DATA] " + e.getMessage());
                } catch (IOException e) {
                    System.err.println(
                            "[LOG ERROR] " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "[CONNECTION CLOSED] " + e.getMessage());
        }
    }
}