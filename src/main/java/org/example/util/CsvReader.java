package org.example.util;

import org.example.model.EnvironmentData;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class CsvReader {

    public static List<EnvironmentData> read(String path) throws Exception {

        List<EnvironmentData> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));

        br.readLine();
        String line;

        while ((line = br.readLine()) != null) {
            String[] v = line.split(",");

            list.add(new EnvironmentData(
                    LocalDateTime.parse(v[0]),
                    Double.parseDouble(v[1]), // temperature
                    Double.parseDouble(v[2]), // humidity
                    Double.parseDouble(v[4]), // windSpeed  (FIXED)
                    Double.parseDouble(v[3]), // pressure   (FIXED)
                    Double.parseDouble(v[5])  // rainfall
            ));
        }

        return list;
    }
}