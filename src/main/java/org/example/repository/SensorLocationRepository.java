package org.example.repository;

import org.example.model.SensorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorLocationRepository
        extends JpaRepository<SensorLocation, Integer> {

    Optional<SensorLocation> findByLatitudeAndLongitude(double latitude, double longitude);
}
