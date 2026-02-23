package org.example.service;

import org.example.model.EnvironmentData;
import java.util.*;

public class Preprocessor {

    public static void smoothTemperature(List<EnvironmentData> data) {

        for (int i = 1; i < data.size() - 1; i++) {

            double avg = (
                    data.get(i - 1).getTemperature() +
                            data.get(i).getTemperature() +
                            data.get(i + 1).getTemperature()
            ) / 3.0;

            data.get(i).setTemperature(avg);
        }
    }
}