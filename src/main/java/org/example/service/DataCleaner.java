package org.example.service;

import org.example.model.EnvironmentData;
import java.util.*;

public class DataCleaner {

    public static List<EnvironmentData> clean(List<EnvironmentData> data) {

        List<EnvironmentData> cleaned = new ArrayList<>();

        for (EnvironmentData d : data) {

            if (d.getTemperature() < -89 || d.getTemperature() > 60) continue;
            if (d.getHumidity() < 0 || d.getHumidity() > 100) continue;
            if (d.getPressure() < 900 || d.getPressure() > 1100) continue;
            if (d.getWindSpeed() < 0 || d.getWindSpeed() > 80) continue;
            if (d.getRainfall() < 0) continue;

            cleaned.add(d);
        }

        return cleaned;
    }
}