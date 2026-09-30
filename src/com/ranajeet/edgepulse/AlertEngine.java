package com.ranajeet.edgepulse;

public class AlertEngine {

    public void evaluate(TelemetryRecord data) {
        if (data.cpu() >= 80) {
            System.out.printf(
                    "[ALERT] %s CPU HIGH: %.1f%%%n",
                    data.deviceId(), data.cpu());
        }

        if (data.memory() >= 85) {
            System.out.printf(
                    "[ALERT] %s MEMORY HIGH: %.1f%%%n",
                    data.deviceId(), data.memory());
        }

        if (data.temperature() >= 75) {
            System.out.printf(
                    "[ALERT] %s TEMPERATURE HIGH: %.1f C%n",
                    data.deviceId(), data.temperature());
        }
    }
}