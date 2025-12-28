package org.example.repository;

import org.example.model.VirtualSensorAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualSensorAlertRepository
        extends JpaRepository<VirtualSensorAlert, Integer> {
}
