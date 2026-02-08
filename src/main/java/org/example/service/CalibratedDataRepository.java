package org.example.service;

import org.example.model.EnvironmentData;
import org.example.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CalibratedDataRepository {

    public static void save(EnvironmentData d) {

        String sql = """
            INSERT INTO calibrated_data
            (time, temperature, humidity, pressure, windSpeed, rainfall)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DatabaseUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.time.toString());
            ps.setDouble(2, d.temperature);
            ps.setDouble(3, d.humidity);
            ps.setDouble(4, d.pressure);
            ps.setDouble(5, d.windSpeed);
            ps.setDouble(6, d.rainfall);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
