package org.example.controller;

import org.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/fetch")
    public String fetchSensorData(
            @RequestParam double lat,
            @RequestParam double lon) {

        try {
            weatherService.fetchAndStoreWeather(lat, lon);
            return "Sensor data fetched and stored successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
