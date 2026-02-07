package org.example.service;

import org.example.model.EnvironmentData;

public class Calibrator {

    public static void calibrate(EnvironmentData d) {

        calibrateTemperature(d);
        calibrateHumidity(d);
        calibratePressure(d);
        calibrateWindSpeed(d);
        calibrateRainfall(d);
    }

    // 🌡 TEMPERATURE
    private static void calibrateTemperature(EnvironmentData d) {
        double bias = 0.5;
        d.temperature -= bias;

        if (d.humidity > 90)
            d.temperature -= 0.3;
    }

    // 💧 HUMIDITY
    private static void calibrateHumidity(EnvironmentData d) {
        if (d.humidity > 100) d.humidity = 100;
        if (d.humidity < 0) d.humidity = 0;
    }

    // 🌡 PRESSURE
    private static void calibratePressure(EnvironmentData d) {
        double altitude = 50;
        d.pressure = d.pressure * Math.exp(altitude / 8434);
    }

    // 🌬 WIND SPEED
    private static void calibrateWindSpeed(EnvironmentData d) {
        if (d.windSpeed > 30)
            d.windSpeed = 30;
    }

    // 🌧 RAINFALL
    private static void calibrateRainfall(EnvironmentData d) {
        if (d.rainfall < 0.2)
            d.rainfall = 0;
    }
}
