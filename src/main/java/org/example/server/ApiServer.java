package org.example.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.EnvironmentData;
import org.example.util.DatabaseUtil;
import org.example.service.DataCleaner;
import org.example.service.Preprocessor;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        get("/cleaned", (req, res) -> {
            res.type("application/json");
            return gson.toJson(getCleanedData());
        });

        get("/preprocessed", (req, res) -> {
            res.type("application/json");
            return gson.toJson(getPreprocessedData());
        });

        get("/calibrated", (req, res) -> {
            res.type("application/json");
            return gson.toJson(getDataWithScores());
        });
    }

    private static List<EnvironmentData> getData() throws Exception {

        List<EnvironmentData> list = new ArrayList<>();

        Connection con = DatabaseUtil.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM calibrated_data");

        while (rs.next()) {
            list.add(new EnvironmentData(
                    rs.getTimestamp("time").toLocalDateTime(),
                    rs.getDouble("temperature"),
                    rs.getDouble("humidity"),
                    rs.getDouble("windSpeed"),
                    rs.getDouble("pressure"),
                    rs.getDouble("rainfall")
            ));
        }

        con.close();
        return list;
    }
    private static List<Map<String, Object>> getDataWithScores() throws Exception {

        List<EnvironmentData> raw = getData();
        List<Map<String, Object>> result = new ArrayList<>();

        org.example.service.IsolationForestService ml =
                new org.example.service.IsolationForestService();

        ml.train(raw);

        for (EnvironmentData d : raw) {

            Map<String, Object> row = new java.util.HashMap<>();

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

        return result;
    }

    private static List<EnvironmentData> getCleanedData() throws Exception {
        List<EnvironmentData> raw = getData();
        return DataCleaner.clean(raw);
    }

    private static List<EnvironmentData> getPreprocessedData() throws Exception {
        List<EnvironmentData> raw = getData();
        Preprocessor.smoothTemperature(raw);
        return raw;
    }
}