package org.example.service;

import org.example.model.EnvironmentData;
import java.util.List;

public class AlertEngine {

    public static void detect(List<EnvironmentData> dataList) throws Exception {

        IsolationForestService ml = new IsolationForestService();
        VoiceService voice = new VoiceService();

        ml.train(dataList);

        for (int i = 1; i < dataList.size(); i++) {

            EnvironmentData current = dataList.get(i);
            EnvironmentData previous = dataList.get(i - 1);

            double score = ml.score(current);
            double pressureDrop =
                    previous.getPressure() - current.getPressure();

            System.out.println("-----------------------------------");
            System.out.println("Time: " + current.getTimestamp());
            System.out.println("Score: " + score);

            if (current.getTemperature() > 45 &&
                    current.getHumidity() < 25 &&
                    current.getWindSpeed() > 25 &&
                    score > 0.6) {

                System.out.println("🔥 FIRE ALERT");
                voice.generateVoice("Critical fire risk detected.");
            }

            else if (pressureDrop > 8 &&
                    current.getWindSpeed() > 40 &&
                    current.getHumidity() > 70) {

                System.out.println("🌪 STORM ALERT");
                voice.generateVoice("Storm pattern detected.");
            }

            else {
                System.out.println("NORMAL");
            }
        }
    }
}