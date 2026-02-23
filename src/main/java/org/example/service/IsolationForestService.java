package org.example.service;

import org.example.model.EnvironmentData;
import smile.anomaly.IsolationForest;

import java.util.List;

public class IsolationForestService {

    private IsolationForest model;

    public void train(List<EnvironmentData> dataList) {

        double[][] data = new double[dataList.size()][5];

        for (int i = 0; i < dataList.size(); i++) {
            EnvironmentData d = dataList.get(i);

            data[i][0] = d.getTemperature();
            data[i][1] = d.getHumidity();
            data[i][2] = d.getWindSpeed();
            data[i][3] = d.getPressure();
            data[i][4] = d.getRainfall();
        }

        model = IsolationForest.fit(data);
    }

    public double score(EnvironmentData d) {

        double[] sample = {
                d.getTemperature(),
                d.getHumidity(),
                d.getWindSpeed(),
                d.getPressure(),
                d.getRainfall()
        };

        return model.score(sample);
    }
}