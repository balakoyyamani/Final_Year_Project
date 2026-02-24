package org.example.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.EnvironmentData;
import org.example.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class ApiServer {

    public static void start() {

        port(8080);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonSerializer<LocalDateTime>)
                                (src, typeOfSrc, context) ->
                                        new com.google.gson.JsonPrimitive(
                                                src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                        ))
                .create();

        get("/calibrated", (req, res) -> {
            res.type("application/json");
            return gson.toJson(getData());
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
}