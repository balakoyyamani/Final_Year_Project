package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.RawEnvironmentData;
import org.example.model.SensorLocation;
import org.example.repository.RawEnvironmentDataRepository;
import org.example.repository.SensorLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    // ===== USER LOCATION =====
    private Double userLat;
    private Double userLon;

    // ===== SENSOR CONTROL FLAG =====
    private boolean sensorRunning = false;

    @Autowired
    private SensorLocationRepository locationRepo;

    @Autowired
    private RawEnvironmentDataRepository dataRepo;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // ==============================
    // START SENSOR (called from controller)
    // ==============================
    public void startSensor(double lat, double lon) {
        this.userLat = lat;
        this.userLon = lon;
        this.sensorRunning = true;

        System.out.println("🟢 Sensor STARTED at: " + lat + ", " + lon);
    }

    // ==============================
    // STOP SENSOR
    // ==============================
    public void stopSensor() {
        this.sensorRunning = false;
        System.out.println("🔴 Sensor STOPPED");
    }

    // ==============================
    // AUTO FETCH EVERY 30 SECONDS
    // ==============================
    @Scheduled(fixedRate = 30000)
    public void autoFetchWeather() throws Exception {

        if (!sensorRunning || userLat == null || userLon == null) {
            return;
        }

        fetchAndStoreWeather(userLat, userLon);
        System.out.println("⏱ Data fetched automatically");
    }

    // ==============================
    // CORE LOGIC
    // ==============================
    public void fetchAndStoreWeather(double lat, double lon) throws Exception {

        String locationName = getLocationName(lat, lon);

        SensorLocation location = locationRepo
                .findByLatitudeAndLongitude(lat, lon)
                .orElseGet(() -> {
                    SensorLocation newLoc = new SensorLocation();
                    newLoc.setLatitude(lat);
                    newLoc.setLongitude(lon);
                    newLoc.setLocationName(locationName);
                    return locationRepo.save(newLoc);
                });

        String apiKey = "0adce9aed614237918b984341abb46d9";  //YOUR_OPENWEATHER_API_KEY

        String weatherUrl =
                "https://api.openweathermap.org/data/2.5/weather"
                        + "?lat=" + lat
                        + "&lon=" + lon
                        + "&units=metric"
                        + "&appid=" + apiKey;

        String weatherJson = restTemplate.getForObject(weatherUrl, String.class);
        JsonNode root = mapper.readTree(weatherJson);

        RawEnvironmentData data = new RawEnvironmentData();
        data.setTemperature(root.path("main").path("temp").asDouble());
        data.setHumidity(root.path("main").path("humidity").asInt());
        data.setAirPressure(root.path("main").path("pressure").asInt());
        data.setWindSpeed(root.path("wind").path("speed").asDouble());
        data.setRainfall(root.path("rain").path("1h").asDouble(0.0));
        data.setLocation(location);

        dataRepo.save(data);
    }

    // ==============================
    // REVERSE GEOCODING
    // ==============================
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
