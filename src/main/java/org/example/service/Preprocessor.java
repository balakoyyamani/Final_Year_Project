package org.example.service;

import org.example.model.EnvironmentData;
import java.util.*;

public class Preprocessor {

    public static void preprocess(List<EnvironmentData> data) {

        for (int i = 1; i < data.size() - 1; i++) {

            data.get(i).temperature =
                    avg(data.get(i-1).temperature,
                            data.get(i).temperature,
                            data.get(i+1).temperature);

            data.get(i).humidity =
                    avg(data.get(i-1).humidity,
                            data.get(i).humidity,
                            data.get(i+1).humidity);

            data.get(i).pressure =
                    avg(data.get(i-1).pressure,
                            data.get(i).pressure,
                            data.get(i+1).pressure);

            data.get(i).windSpeed =
                    avg(data.get(i-1).windSpeed,
                            data.get(i).windSpeed,
                            data.get(i+1).windSpeed);

            // rainfall is accumulated, not smoothed
        }
    }

    private static double avg(double a, double b, double c) {
        return (a + b + c) / 3;
    }
}
