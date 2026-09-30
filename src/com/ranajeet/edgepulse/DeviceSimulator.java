package com.ranajeet.edgepulse;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DeviceSimulator {

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 5050;
        String deviceId = args.length > 0
                ? args[0] : "device-01";

        Random random = new Random();

        try (Socket socket = new Socket(host, port);
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(
                             socket.getOutputStream(),
                             StandardCharsets.UTF_8))) {

            for (int i = 0; i < 20; i++) {
                double cpu = random.nextDouble() * 100;
                double memory = 25 + random.nextDouble() * 75;
                double temperature = 35 + random.nextDouble() * 50;
                double latency = 2 + random.nextDouble() * 150;

                String message = String.format(
                        java.util.Locale.ROOT,
                        "%s|%d|%.2f|%.2f|%.2f|%.2f",
                        deviceId,
                        System.currentTimeMillis(),
                        cpu,
                        memory,
                        temperature,
                        latency);

                writer.write(message);
                writer.newLine();
                writer.flush();

                System.out.println("Sent: " + message);

                Thread.sleep(500);
            }
        }

        System.out.println("Simulation complete.");
    }
}