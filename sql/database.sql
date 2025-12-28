CREATE DATABASE virtual_sensor_system;

USE virtual_sensor_system;

CREATE TABLE sensor_location (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DECIMAL(8,5) NOT NULL,
    longitude DECIMAL(8,5) NOT NULL,
    location_name VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE raw_environment_data (
    data_id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT NOT NULL,

    temperature DECIMAL(5,2),
    humidity INT,
    wind_speed DECIMAL(5,2),
    rainfall DECIMAL(5,2),
    air_pressure INT,

    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_location
    FOREIGN KEY (location_id)
    REFERENCES sensor_location(location_id)
    ON DELETE CASCADE
);

CREATE TABLE virtual_sensor_output (
    output_id INT AUTO_INCREMENT PRIMARY KEY,
    data_id INT NOT NULL,

    risk_type VARCHAR(50),
    risk_level VARCHAR(20),
    description TEXT,

    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_raw_data
    FOREIGN KEY (data_id)
    REFERENCES raw_environment_data(data_id)
    ON DELETE CASCADE
);

ALTER TABLE sensor_location
ADD CONSTRAINT unique_lat_lon UNIQUE (latitude, longitude);


CREATE TABLE virtual_sensor_alert (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT,
    alert_type VARCHAR(50),
    severity VARCHAR(20),
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (location_id) REFERENCES sensor_location(location_id)
);
