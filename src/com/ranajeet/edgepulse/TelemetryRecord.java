
package com.ranajeet.edgepulse;

public class TelemetryRecord {

    private final String deviceId;
    private final long timestamp;
    private final double cpu;
    private final double memory;
    private final double temperature;
    private final double latency;

    public TelemetryRecord(String deviceId, long timestamp,
                           double cpu, double memory,
                           double temperature, double latency) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.cpu = cpu;
        this.memory = memory;
        this.temperature = temperature;
        this.latency = latency;
    }

    public String deviceId() { return deviceId; }
    public long timestamp() { return timestamp; }
    public double cpu() { return cpu; }
    public double memory() { return memory; }
    public double temperature() { return temperature; }
    public double latency() { return latency; }

    public static TelemetryRecord parse(String line) {
        String[] p = line.trim().split("\\|");

        if (p.length != 6) {
            throw new IllegalArgumentException("Invalid message format");
        }

        String id = p[0].trim();
        long time = Long.parseLong(p[1]);
        double cpu = Double.parseDouble(p[2]);
        double memory = Double.parseDouble(p[3]);
        double temperature = Double.parseDouble(p[4]);
        double latency = Double.parseDouble(p[5]);

        if (id.isEmpty()
                || time < 0
                || !Double.isFinite(cpu)
                || !Double.isFinite(memory)
                || !Double.isFinite(temperature)
                || !Double.isFinite(latency)
                || cpu < 0 || cpu > 100
                || memory < 0 || memory > 100
                || latency < 0) {
            throw new IllegalArgumentException("Invalid telemetry values");
        }

        return new TelemetryRecord(
                id, time, cpu, memory, temperature, latency);
    }
}