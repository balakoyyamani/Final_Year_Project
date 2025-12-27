package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor_location")
public class SensorLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    private double latitude;
    private double longitude;
    private String locationName;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
