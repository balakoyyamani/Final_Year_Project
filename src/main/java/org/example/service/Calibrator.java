package org.example.service;

import org.example.model.EnvironmentData;

public class Calibrator {

    public static void calibrate(EnvironmentData d) {

        // 1️⃣ TEMPERATURE CALIBRATION
        // Bias correction
        d.temperature = d.temperature - 0.5;

        // Context-aware adjustment
        if (d.humidity > 90) {
            d.temperature -= 0.3;
        }

        // 2️⃣ HUMIDITY CALIBRATION
        // Clamp to physical limits
        if (d.humidity < 0) d.humidity = 0;
        if (d.humidity > 100) d.humidity = 100;

        // Slight sensor bias correction
        d.humidity = d.humidity - 1.0;

        // 3️⃣ PRESSURE CALIBRATION
        // Sea-level normalization (light correction)
        d.pressure = d.pressure + 1.5;

        // Smooth sudden pressure drops (storm noise)
        if (d.pressure < 980) {
            d.pressure += 2.0;
        }

        // 4️⃣ WIND SPEED CALIBRATION
        // Remove unrealistic spikes
        if (d.windSpeed > 20) {
            d.windSpeed = 20;
        }

        // Mechanical inertia simulation
        d.windSpeed = d.windSpeed * 0.95;

        // 5️⃣ RAINFALL CALIBRATION
        // Noise threshold
        if (d.rainfall < 0.2) {
            d.rainfall = 0.0;
        }

        // Accumulation correction
        d.rainfall = d.rainfall * 1.05;

        // 🔥 FINAL ROUNDING (1 DECIMAL ONLY)
        d.temperature = round1(d.temperature);
        d.humidity = round1(d.humidity);
        d.pressure = round1(d.pressure);
        d.windSpeed = round1(d.windSpeed);
        d.rainfall = round1(d.rainfall);
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
