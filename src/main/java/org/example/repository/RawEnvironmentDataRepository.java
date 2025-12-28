package org.example.repository;

import org.example.model.RawEnvironmentData;
import org.example.model.SensorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawEnvironmentDataRepository
        extends JpaRepository<RawEnvironmentData, Integer> {

    // Fetch last 5 sensor readings for a location (latest first)
    List<RawEnvironmentData>
    findTop5ByLocationOrderByRecordedAtDesc(SensorLocation location);
}
