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

    // ===== USER PROVIDED LOCATION (SET ONCE) =====
    private Double userLat;
    private Double userLon;

    @Autowired
    private SensorLocationRepository locationRepo;

    @Autowired
    private RawEnvironmentDataRepository dataRepo;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // ==============================
    // 1️⃣ SET USER LOCATION (CALLED ONCE)
    // ==============================
    public void setUserLocation(double lat, double lon) {
        this.userLat = lat;
        this.userLon = lon;
        System.out.println("📍 Virtual sensor started at: " + lat + ", " + lon);
    }

    // ==============================
    // 2️⃣ AUTO FETCH EVERY 30 SECONDS
    // ==============================
    @Scheduled(fixedRate = 30000) // 30 seconds
    public void autoFetchWeather() throws Exception {

        if (userLat == null || userLon == null) {
            return; // user not set yet
        }

        fetchAndStoreWeather(userLat, userLon);
        System.out.println("⏱ Weather data fetched automatically");
    }

    // ==============================
    // 3️⃣ MAIN LOGIC (API + DB)
    // ==============================
    public void fetchAndStoreWeather(double lat, double lon) throws Exception {

        // ---- Reverse Geocoding (Location Name) ----
        String locationName = getLocationName(lat, lon);

        // ---- Prevent Duplicate Location Insert ----
        SensorLocation location = locationRepo
                .findByLatitudeAndLongitude(lat, lon)
                .orElseGet(() -> {
                    SensorLocation newLocation = new SensorLocation();
                    newLocation.setLatitude(lat);
                    newLocation.setLongitude(lon);
                    newLocation.setLocationName(locationName);
                    return locationRepo.save(newLocation);
                });

        // ---- Weather API Call ----
        String apiKey = "0adce9aed614237918b984341abb46d9"; //YOUR_OPENWEATHER_API_KEY

        String weatherUrl =
                "https://api.openweathermap.org/data/2.5/weather"
                        + "?lat=" + lat
                        + "&lon=" + lon
                        + "&units=metric"
                        + "&appid=" + apiKey;

        String weatherJson = restTemplate.getForObject(weatherUrl, String.class);
        JsonNode root = mapper.readTree(weatherJson);

        // ---- Store Raw Sensor Data ----
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
    // 4️⃣ REVERSE GEOCODING (FREE API)
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
