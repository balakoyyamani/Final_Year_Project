package org.example.service;

import org.example.model.EnvironmentData;
import java.util.*;

public class AlertEngine {

    public static void detect(List<EnvironmentData> data) {

        for (int i = 3; i < data.size(); i++) {

            double tempTrend = data.get(i).temperature -
                    data.get(i-3).temperature;

            double pressureDrop = data.get(i).pressure -
                    data.get(i-3).pressure;

            int score = 0;

            if (tempTrend > 4) score++;
            if (pressureDrop < -5) score++;
            if (data.get(i).rainfall > 5) score++;

            if (score >= 2) {
                System.out.println("⚠ ALERT at " + data.get(i).time +
                        " | Confidence: " + (score * 33) + "%");
            }
        }
    }
}
