package org.example.model;

import java.time.LocalDateTime;

public class EnvironmentData {

    private LocalDateTime timestamp;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private double pressure;
    private double rainfall;

    public EnvironmentData(LocalDateTime timestamp,
                           double temperature,
                           double humidity,
                           double windSpeed,
                           double pressure,
                           double rainfall) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.rainfall = rainfall;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public double getPressure() { return pressure; }
    public double getRainfall() { return rainfall; }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setRainfall(double rainfall) {
        this.rainfall = rainfall;
    }

    @Override
    public String toString() {
        return timestamp +
                " | T=" + temperature +
                " H=" + humidity +
                " W=" + windSpeed +
                " P=" + pressure +
                " R=" + rainfall;
    }
}