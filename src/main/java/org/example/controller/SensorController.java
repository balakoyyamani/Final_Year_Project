package org.example.controller;

import org.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private WeatherService weatherService;

    // Trigger API call + save data using default lat/lon
    @GetMapping("/fetch")
    public String fetchAndStoreSensorData() {
        try {
            weatherService.fetchAndStoreWeather();
            return "Weather data fetched and stored successfully";
        } catch (Exception e) {
            return "Error while fetching weather data: " + e.getMessage();
        }
    }
}
