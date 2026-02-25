package org.example.service;

import org.example.model.EnvironmentData;
import java.util.*;

public class Preprocessor {

    public static void smoothAllSensors(List<EnvironmentData> data) {

        if (data.size() < 3) return;

        List<Double> temp = new ArrayList<>();
        List<Double> hum = new ArrayList<>();
        List<Double> pres = new ArrayList<>();
        List<Double> wind = new ArrayList<>();
        List<Double> rain = new ArrayList<>();

        for (EnvironmentData d : data) {
            temp.add(d.getTemperature());
            hum.add(d.getHumidity());
            pres.add(d.getPressure());
            wind.add(d.getWindSpeed());
            rain.add(d.getRainfall());
        }

        for (int i = 1; i < data.size() - 1; i++) {

            double avgTemp = (temp.get(i - 1) + temp.get(i) + temp.get(i + 1)) / 3.0;
            double avgHum  = (hum.get(i - 1) + hum.get(i) + hum.get(i + 1)) / 3.0;
            double avgPres = (pres.get(i - 1) + pres.get(i) + pres.get(i + 1)) / 3.0;
            double avgWind = (wind.get(i - 1) + wind.get(i) + wind.get(i + 1)) / 3.0;
            double avgRain = (rain.get(i - 1) + rain.get(i) + rain.get(i + 1)) / 3.0;

            data.get(i).setTemperature(round(avgTemp));
            data.get(i).setHumidity(round(avgHum));
            data.get(i).setPressure(round(avgPres));
            data.get(i).setWindSpeed(round(avgWind));
            data.get(i).setRainfall(round(avgRain));
        }
    }

    private static double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}