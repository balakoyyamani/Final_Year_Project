package org.example;
//Version 5
import org.example.model.EnvironmentData;
import org.example.service.*;
import org.example.util.CsvReader;
import org.example.server.ApiServer;


import java.util.List;

public class Main {

    public static void main(String[] args) {

        ApiServer.start();
        try {

            // 1️⃣ Select dataset
            //String dataset = "environment";
            String dataset = "fire_risk";
            //String dataset = "out_of_range";

            String csvPath = "src/main/resources/" + dataset + ".csv";

            // 2️⃣ Read RAW data
            List<EnvironmentData> rawData = CsvReader.read(csvPath);

            System.out.println("========== RAW DATA ==========");
            rawData.forEach(System.out::println);

            // 3️⃣ Clean data
            List<EnvironmentData> cleanData =
                    DataCleaner.clean(rawData);

            System.out.println("\n========== CLEANED DATA ==========");

            // 4️⃣ Preprocess (Smoothing)
            Preprocessor.smoothTemperature(cleanData);

            // 5️⃣ Calibrate + Store in DB
            System.out.println("\n========== CALIBRATED DATA (Stored in DB) ==========");

            for (EnvironmentData d : cleanData) {

                Calibrator.calibrate(d);

                CalibratedDataRepository.save(d);

                System.out.println(d);
            }

            // 6️⃣ Run Isolation Forest + Alert Engine
            System.out.println("\n========== ALERT ENGINE ==========");
            AlertEngine.detect(cleanData);

            System.out.println("\nPROCESS COMPLETED SUCCESSFULLY");

        } catch (Exception e) {

            System.out.println("ERROR OCCURRED");
            e.printStackTrace();
        }
    }
}