package org.example.controller;

import org.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private WeatherService weatherService;

    // User provides latitude & longitude ONCE
    @PostMapping("/start")
    public String startVirtualSensor(
            @RequestParam double lat,
            @RequestParam double lon) {

        weatherService.setUserLocation(lat, lon);
        return "Virtual sensor started. Data will be fetched every 30 seconds.";
    }
}
