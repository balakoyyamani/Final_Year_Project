package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.RawEnvironmentData;
import org.example.model.SensorLocation;
import org.example.repository.RawEnvironmentDataRepository;
import org.example.repository.SensorLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Autowired
    private SensorLocationRepository locationRepo;

    @Autowired
    private RawEnvironmentDataRepository dataRepo;

    public void fetchAndStoreWeather() throws Exception {

        double lat = 13.0827;
        double lon = 80.2707;

        String apiKey = "0adce9aed614237918b984341abb46d9"; //API_KEY

        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&units=metric"
                + "&appid=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        SensorLocation location = new SensorLocation();
        location.setLatitude(lat);
        location.setLongitude(lon);
        location.setLocationName("Default Chennai Location");
        locationRepo.save(location);

        RawEnvironmentData data = new RawEnvironmentData();
        data.setTemperature(root.path("main").path("temp").asDouble());
        data.setHumidity(root.path("main").path("humidity").asInt());
        data.setAirPressure(root.path("main").path("pressure").asInt());
        data.setWindSpeed(root.path("wind").path("speed").asDouble());
        data.setRainfall(root.path("rain").path("1h").asDouble(0.0));
        data.setLocation(location);

        dataRepo.save(data);

        System.out.println("✅ Weather data stored successfully");
    }
}
