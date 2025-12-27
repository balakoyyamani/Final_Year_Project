package org.example.repository;

import org.example.model.SensorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorLocationRepository
        extends JpaRepository<SensorLocation, Integer> {
}
