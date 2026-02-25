package org.example.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.EnvironmentData;
import org.example.service.Calibrator;
import org.example.service.DataCleaner;
import org.example.service.Preprocessor;
import org.example.service.IsolationForestService;
import org.example.util.CsvReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static spark.Spark.*;

public class ApiServer {

    public static void start() {

        port(8080);

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "*");
            res.header("Access-Control-Allow-Headers", "*");
        });

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonSerializer<LocalDateTime>)
                                (src, typeOfSrc, context) ->
                                        new com.google.gson.JsonPrimitive(
                                                src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                        ))
                .create();

        /* ---------------- RAW ---------------- */

        get("/raw", (req, res) -> {
            res.type("application/json");
            return gson.toJson(getRawData());
        });

        /* ---------------- CLEANED ---------------- */

        get("/cleaned", (req, res) -> {
            res.type("application/json");

            List<EnvironmentData> raw = getRawData();
            List<EnvironmentData> cleaned = DataCleaner.clean(raw);

            return gson.toJson(cleaned);
        });

        /* ---------------- PREPROCESSED ---------------- */

        get("/preprocessed", (req, res) -> {
            res.type("application/json");

            List<EnvironmentData> raw = getRawData();
            List<EnvironmentData> cleaned = DataCleaner.clean(raw);

           // Preprocessor.smoothTemperature(cleaned);
            Preprocessor.smoothAllSensors(cleaned);

            return gson.toJson(cleaned);
        });

        /* ---------------- CALIBRATED + SCORED ---------------- */

        get("/calibrated", (req, res) -> {
            res.type("application/json");

            List<EnvironmentData> raw = getRawData();
            List<EnvironmentData> cleaned = DataCleaner.clean(raw);

            //Preprocessor.smoothTemperature(cleaned);
            Preprocessor.smoothAllSensors(cleaned);

            for (EnvironmentData d : cleaned) {
                Calibrator.calibrate(d);
            }

            IsolationForestService ml = new IsolationForestService();
            ml.train(cleaned);

            List<Map<String, Object>> result = new ArrayList<>();

            for (EnvironmentData d : cleaned) {

                Map<String, Object> row = new HashMap<>();

                row.put("timestamp", d.getTimestamp());
                row.put("temperature", d.getTemperature());
                row.put("humidity", d.getHumidity());
                row.put("windSpeed", d.getWindSpeed());
                row.put("pressure", d.getPressure());
                row.put("rainfall", d.getRainfall());

                double score = ml.score(d);
                row.put("score", score);

                result.add(row);
            }

            return gson.toJson(result);
        });

        System.out.println("API running at http://localhost:8080");
    }

    /* ---------------- READ RAW FROM CSV ---------------- */

    private static List<EnvironmentData> getRawData() throws Exception {

        String dataset = "environment";
        // String dataset = "fire_risk";
        //String dataset = "out_of_range";
        String path = "src/main/resources/" + dataset + ".csv";

        return CsvReader.read(path);
    }
}