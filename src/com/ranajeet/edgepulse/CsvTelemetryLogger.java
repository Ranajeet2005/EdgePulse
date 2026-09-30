package com.ranajeet.edgepulse;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

public class CsvTelemetryLogger {

    private final Path file = Path.of("telemetry_log.csv");

    public CsvTelemetryLogger() throws IOException {
        if (Files.notExists(file) || Files.size(file) == 0) {
            Files.writeString(file,
                    "device_id,timestamp,cpu,memory,temperature,latency\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        }
    }

    public synchronized void log(TelemetryRecord data)
            throws IOException {

        String row = String.format(
                java.util.Locale.ROOT,
                "\"%s\",%s,%.2f,%.2f,%.2f,%.2f%n",
                data.deviceId().replace("\"", "\"\""),
                Instant.ofEpochMilli(data.timestamp()),
                data.cpu(),
                data.memory(),
                data.temperature(),
                data.latency());

        Files.writeString(file, row,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }
}