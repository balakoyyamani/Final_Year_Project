package org.example.controller;

import org.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/data")
    public String getSensorData(
            @RequestParam double lat,
            @RequestParam double lon) {

        return weatherService.fetchWeather(lat, lon);
    }
}
