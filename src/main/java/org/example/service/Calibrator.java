package org.example.service;

import org.example.model.EnvironmentData;

public class Calibrator {

    public static void calibrate(EnvironmentData d) {

        double temp = d.getTemperature() - 0.5;

        if (d.getHumidity() > 90) {
            temp -= 0.3;
        }

        double humidity = d.getHumidity() - 1.0;
        if (humidity < 0) humidity = 0;
        if (humidity > 100) humidity = 100;

        double pressure = d.getPressure() + 1.5;
        if (pressure < 980) pressure += 2.0;

        double wind = d.getWindSpeed();
        if (wind > 20) wind = 20;
        wind = wind * 0.95 - 1;

        double rain = d.getRainfall();
        if (rain < 0.2) rain = 0;
        rain = rain * 1.05;

        // rounding
        temp = round(temp);
        humidity = round(humidity);
        pressure = round(pressure);
        wind = round(wind);
        rain = round(rain);

        // SET back (you need setters for all OR modify model)
        d.setTemperature(temp);
    }

    private static double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}