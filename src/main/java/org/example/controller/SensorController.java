package org.example.controller;

import org.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private WeatherService weatherService;

    // START SENSOR
    @PostMapping("/start")
    public String startSensor(
            @RequestParam double lat,
            @RequestParam double lon) {

        weatherService.startSensor(lat, lon);
        return "🟢 Virtual sensor started";
    }

    // STOP SENSOR
    @PostMapping("/stop")
    public String stopSensor() {

        weatherService.stopSensor();
        return "🔴 Virtual sensor stopped";
    }
}
