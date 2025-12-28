package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.RawEnvironmentData;
import org.example.model.SensorLocation;
import org.example.model.VirtualSensorAlert;
import org.example.repository.RawEnvironmentDataRepository;
import org.example.repository.SensorLocationRepository;
import org.example.repository.VirtualSensorAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeatherService {

    private Double userLat;
    private Double userLon;
    private boolean sensorRunning = false;

    @Autowired
    private SensorLocationRepository locationRepo;

    @Autowired
    private RawEnvironmentDataRepository dataRepo;

    @Autowired
    private VirtualSensorAlertRepository alertRepo;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // ==============================
    // START / STOP SENSOR
    // ==============================
    public void startSensor(double lat, double lon) {
        this.userLat = lat;
        this.userLon = lon;
        this.sensorRunning = true;
        System.out.println("🟢 Sensor started at " + lat + ", " + lon);
    }

    public void stopSensor() {
        this.sensorRunning = false;
        System.out.println("🔴 Sensor stopped");
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
    }

    // ==============================
    // CORE PIPELINE
    // ==============================
    public void fetchAndStoreWeather(double lat, double lon) throws Exception {

        String locationName = getLocationName(lat, lon);

        SensorLocation location = locationRepo
                .findByLatitudeAndLongitude(lat, lon)
                .orElseGet(() -> {
                    SensorLocation loc = new SensorLocation();
                    loc.setLatitude(lat);
                    loc.setLongitude(lon);
                    loc.setLocationName(locationName);
                    return locationRepo.save(loc);
                });

        String apiKey = "0adce9aed614237918b984341abb46d9"; //YOUR_OPENWEATHER_API_KEY

        String url =
                "https://api.openweathermap.org/data/2.5/weather"
                        + "?lat=" + lat
                        + "&lon=" + lon
                        + "&units=metric"
                        + "&appid=" + apiKey;

        String json = restTemplate.getForObject(url, String.class);
        JsonNode root = mapper.readTree(json);

        RawEnvironmentData data = new RawEnvironmentData();
        data.setTemperature(root.path("main").path("temp").asDouble());
        data.setHumidity(root.path("main").path("humidity").asInt());
        data.setAirPressure(root.path("main").path("pressure").asInt());
        data.setWindSpeed(root.path("wind").path("speed").asDouble());
        data.setRainfall(root.path("rain").path("1h").asDouble(0.0));
        data.setLocation(location);

        dataRepo.save(data);

        // SMART ALERT LOGIC
        generateSmartAlerts(location);
    }

    // ==============================
    // SEASON + TREND BASED ALERTS
    // ==============================
    private void generateSmartAlerts(SensorLocation location) {

        List<RawEnvironmentData> last5 =
                dataRepo.findTop5ByLocationOrderByRecordedAtDesc(location);

        if (last5.size() < 5) return;

        Season season = detectSeason();
        RawEnvironmentData latest = last5.get(0);

        boolean tempRising = isIncreasing(
                last5.stream().map(RawEnvironmentData::getTemperature).toList()
        );

        boolean humidityDropping = isDecreasing(
                last5.stream().map(RawEnvironmentData::getHumidity).toList()
        );

        // 🔥 FIRE RISK (Season + Trend)
        if (season == Season.SUMMER
                && tempRising
                && humidityDropping
                && latest.getTemperature() > 36
                && latest.getHumidity() < 35
                && latest.getWindSpeed() > 5) {

            saveAlert(
                    "Fire Risk",
                    "HIGH",
                    "Rising temperature and falling humidity detected in summer season",
                    location
            );
        }

        // 🌊 FLOOD RISK (Monsoon)
        double avgRain = last5.stream()
                .mapToDouble(RawEnvironmentData::getRainfall)
                .average().orElse(0);

        if (season == Season.MONSOON && avgRain > 15) {

            saveAlert(
                    "Flood Risk",
                    "HIGH",
                    "Continuous rainfall trend detected during monsoon",
                    location
            );
        }

        // 🌪 STORM RISK
        if (latest.getWindSpeed() > 15 && latest.getAirPressure() < 990) {

            saveAlert(
                    "Storm Risk",
                    "MEDIUM",
                    "High wind speed with low atmospheric pressure",
                    location
            );
        }

        // ⚠ HEAT ALERT
        if (latest.getTemperature() > 42) {

            saveAlert(
                    "Heat Alert",
                    "MEDIUM",
                    "Extreme temperature detected",
                    location
            );
        }
    }

    // ==============================
    // ALERT SAVE
    // ==============================
    private void saveAlert(String type, String severity,
                           String message, SensorLocation location) {

        VirtualSensorAlert alert = new VirtualSensorAlert();
        alert.setAlertType(type);
        alert.setSeverity(severity);
        alert.setMessage(message);
        alert.setLocation(location);

        alertRepo.save(alert);

        System.out.println("🚨 ALERT: " + type + " (" + severity + ")");
    }

    // ==============================
    // SEASON DETECTION
    // ==============================
    private Season detectSeason() {

        int month = LocalDate.now().getMonthValue();

        if (month >= 3 && month <= 6) return Season.SUMMER;
        if (month >= 7 && month <= 10) return Season.MONSOON;
        return Season.WINTER;
    }

    private enum Season {
        SUMMER, MONSOON, WINTER
    }

    // ==============================
    // TREND HELPERS
    // ==============================
    private boolean isIncreasing(List<Double> values) {
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) <= values.get(i - 1)) return false;
        }
        return true;
    }

    private boolean isDecreasing(List<Integer> values) {
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) >= values.get(i - 1)) return false;
        }
        return true;
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
