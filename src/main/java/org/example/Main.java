package org.example;

import org.example.model.EnvironmentData;
import org.example.service.*;
import org.example.util.CsvReader;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        List<EnvironmentData> raw =
                CsvReader.read("src/main/resources/environment.csv");

        System.out.println("RAW DATA");
        raw.forEach(System.out::println);

        List<EnvironmentData> clean = DataCleaner.clean(raw);
        Preprocessor.preprocess(clean);

        for (EnvironmentData d : clean) {
            Calibrator.calibrate(d);
        }

        System.out.println("\nCALIBRATED DATA");
        clean.forEach(System.out::println);

        System.out.println("\nALERTS");
        AlertEngine.detect(clean);
    }
}