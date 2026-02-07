package org.example.service;

import org.example.model.EnvironmentData;

public class Calibrator {

    private static final double TEMP_BIAS = 0.5;

    public static void calibrate(EnvironmentData d) {
        d.temperature = d.temperature - TEMP_BIAS;

        if (d.rainfall < 0.2)
            d.rainfall = 0;

        if (d.humidity > 90)
            d.temperature -= 0.3;
    }
}
