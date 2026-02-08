package org.example.service;

import org.example.model.EnvironmentData;

public class Calibrator {

    private static final double TEMP_BIAS = 0.5;

    public static void calibrate(EnvironmentData d) {

        // Temperature bias correction
        d.temperature = d.temperature - TEMP_BIAS;

        // Context-aware adjustment
        if (d.humidity > 90) {
            d.temperature -= 0.3;
        }

        // Rainfall noise removal
        if (d.rainfall < 0.2) {
            d.rainfall = 0.0;
        }

        // 🔥 ROUND ALL VALUES TO 1 DECIMAL
        d.temperature = round1(d.temperature);
        d.humidity = round1(d.humidity);
        d.pressure = round1(d.pressure);
        d.windSpeed = round1(d.windSpeed);
        d.rainfall = round1(d.rainfall);
    }

    private static double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
