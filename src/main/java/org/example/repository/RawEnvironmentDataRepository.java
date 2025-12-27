package org.example.repository;

import org.example.model.RawEnvironmentData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawEnvironmentDataRepository
        extends JpaRepository<RawEnvironmentData, Integer> {
}
