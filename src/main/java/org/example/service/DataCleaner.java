package org.example.service;

import org.example.model.EnvironmentData;
import java.util.*;

public class DataCleaner {

    public static List<EnvironmentData> clean(List<EnvironmentData> data) {
        List<EnvironmentData> clean = new ArrayList<>();

        for (EnvironmentData d : data) {
            if (d.temperature < -10 || d.temperature > 60) continue;
            if (d.humidity < 0 || d.humidity > 100) continue;
            if (d.pressure < 900 || d.pressure > 1100) continue;

            clean.add(d);
        }
        return clean;
    }
}
