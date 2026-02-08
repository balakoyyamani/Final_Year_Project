package org.example.service;

import org.example.model.EnvironmentData;
import java.util.List;

public class Preprocessor {

    public static void smoothTemperature(List<EnvironmentData> data) {

        for (int i = 1; i < data.size() - 1; i++) {

            double avg = (
                    data.get(i - 1).temperature +
                            data.get(i).temperature +
                            data.get(i + 1).temperature
            ) / 3.0;

            data.get(i).temperature = avg;
        }
    }
}
