package org.example.model;

import java.time.LocalDateTime;

public class EnvironmentData {

    public LocalDateTime time;
    public double temperature;
    public double humidity;
    public double pressure;
    public double windSpeed;
    public double rainfall;

    public EnvironmentData(LocalDateTime time, double temperature, double humidity,
                           double pressure, double windSpeed, double rainfall) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.rainfall = rainfall;
    }

    @Override
    public String toString() {
        return time + " | T=" + temperature +
                " H=" + humidity +
                " P=" + pressure +
                " W=" + windSpeed +
                " R=" + rainfall;
    }
}