package org.example.service;

import org.example.model.EnvironmentData;
import org.example.util.DatabaseUtil;

import java.sql.*;

public class CalibratedDataRepository {

    public static void clearTable() throws Exception {

        Connection con = DatabaseUtil.getConnection();

        Statement st = con.createStatement();

        // Delete all rows + reset auto increment
        st.executeUpdate("TRUNCATE TABLE calibrated_data");

        con.close();
    }

    public static void save(EnvironmentData d) throws Exception {

        Connection con = DatabaseUtil.getConnection();

        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO calibrated_data (time, temperature, humidity, pressure, windSpeed, rainfall) VALUES (?,?,?,?,?,?)"
        );

        ps.setTimestamp(1, Timestamp.valueOf(d.getTimestamp()));
        ps.setDouble(2, d.getTemperature());
        ps.setDouble(3, d.getHumidity());
        ps.setDouble(4, d.getPressure());
        ps.setDouble(5, d.getWindSpeed());
        ps.setDouble(6, d.getRainfall());

        ps.executeUpdate();
        con.close();
    }
}