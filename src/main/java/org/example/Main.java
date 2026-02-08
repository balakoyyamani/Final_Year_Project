package org.example;

import org.example.model.EnvironmentData;
import org.example.service.AlertEngine;
import org.example.service.CalibratedDataRepository;
import org.example.service.Calibrator;
import org.example.service.DataCleaner;
import org.example.service.Preprocessor;
import org.example.util.CsvReader;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            // 1️⃣ Read RAW data from CSV
            List<EnvironmentData> rawData =
                    CsvReader.read("src/main/resources/environment.csv");

            System.out.println("RAW DATA");
            rawData.forEach(System.out::println);

            // 2️⃣ Clean data
            List<EnvironmentData> cleanData =
                    DataCleaner.clean(rawData);

            // 3️⃣ Preprocess (smoothing)
            Preprocessor.smoothTemperature(cleanData);

            // 4️⃣ Calibrate + Store into MySQL
            System.out.println("\nCALIBRATED DATA (STORED IN DB)");
            for (EnvironmentData d : cleanData) {
                Calibrator.calibrate(d);
                CalibratedDataRepository.save(d);
                System.out.println(d);
            }

            // 5️⃣ Alert detection
            System.out.println("\nALERTS");
            AlertEngine.detect(cleanData);

            System.out.println("\nPROCESS COMPLETED SUCCESSFULLY");

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED");
            e.printStackTrace();
        }
    }
}
