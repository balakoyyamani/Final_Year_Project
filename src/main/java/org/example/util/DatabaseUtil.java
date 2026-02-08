package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

    private static final String URL =
            "jdbc:mysql://localhost:3306/virtual_sensor_db";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
