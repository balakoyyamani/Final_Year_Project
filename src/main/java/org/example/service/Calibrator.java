package org.example.service;

import org.example.model.EnvironmentData;

public class Calibrator {

    public static void calibrate(EnvironmentData d) {

        // 1️⃣ TEMPERATURE CALIBRATION
        // Bias correction
        double temp_bias=0.5;
        d.temperature = d.temperature - temp_bias;

        // Context-aware adjustment
        if (d.humidity > 90) {
            d.temperature -= 0.3;
        }

        // 2️⃣ HUMIDITY CALIBRATION
        // Clamp to physical limits
        double hum_bias=1.0;
        if (d.humidity < 0) d.humidity = 0;
        if (d.humidity > 100) d.humidity = 100;

        // Slight sensor bias correction
        d.humidity = d.humidity - hum_bias;

        // 3️⃣ PRESSURE CALIBRATION
        // Sea-level normalization (light correction)
        double pres_bias=1.5;
        d.pressure = d.pressure + pres_bias;

        // Smooth sudden pressure drops (storm noise)
        if (d.pressure < 980) {
            d.pressure += 2.0;
        }

        // 4️⃣ WIND SPEED CALIBRATION
        // Remove unrealistic spikes
        double wind_bias=1;

        if (d.windSpeed > 20) {
            d.windSpeed = 20;
        }

        // Mechanical inertia simulation
        d.windSpeed = d.windSpeed * 0.95;
        d.windSpeed=d.windSpeed-wind_bias;

        // 5️⃣ RAINFALL CALIBRATION
        // Noise threshold
        double rain_bias=0.0;
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
