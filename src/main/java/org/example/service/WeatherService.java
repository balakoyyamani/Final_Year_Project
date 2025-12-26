package org.example.service;

import org.springframework.web.client.RestTemplate;

public class WeatherService {
    public String fetchWeather(double lat, double lon) {
        String apiKey = "0adce9aed614237918b984341abb46d9"; //Weather API key

        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&units=metric"
                + "&appid=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
