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

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public void fetchAndStoreWeather(double lat, double lon) throws Exception {

        String locationName = getLocationName(lat, lon);

        String apiKey = "0adce9aed614237918b984341abb46d9";  //YOUR_OPENWEATHER_API_KEY

        String weatherUrl =
                "https://api.openweathermap.org/data/2.5/weather"
                        + "?lat=" + lat
                        + "&lon=" + lon
                        + "&units=metric"
                        + "&appid=" + apiKey;

        String weatherJson = restTemplate.getForObject(weatherUrl, String.class);
        JsonNode root = mapper.readTree(weatherJson);

        SensorLocation location = new SensorLocation();
        location.setLatitude(lat);
        location.setLongitude(lon);
        location.setLocationName(locationName);
        locationRepo.save(location);

        RawEnvironmentData data = new RawEnvironmentData();
        data.setTemperature(root.path("main").path("temp").asDouble());
        data.setHumidity(root.path("main").path("humidity").asInt());
        data.setAirPressure(root.path("main").path("pressure").asInt());
        data.setWindSpeed(root.path("wind").path("speed").asDouble());
        data.setRainfall(root.path("rain").path("1h").asDouble(0.0));
        data.setLocation(location);

        dataRepo.save(data);
    }

    private String getLocationName(double lat, double lon) throws Exception {

        String url =
                "https://nominatim.openstreetmap.org/reverse"
                        + "?format=json"
                        + "&lat=" + lat
                        + "&lon=" + lon;

        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = mapper.readTree(response);

        return root.path("display_name").asText("Unknown Location");
    }
}
