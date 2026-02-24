CREATE DATABASE virtual_sensor_db;

USE virtual_sensor_db;

CREATE TABLE calibrated_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    time VARCHAR(30),
    temperature DOUBLE,
    humidity DOUBLE,
    pressure DOUBLE,
    windSpeed DOUBLE,
    rainfall DOUBLE
);

DROP TABLE calibrated_data;

CREATE TABLE calibrated_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    time DATETIME,
    temperature DOUBLE,
    humidity DOUBLE,
    pressure DOUBLE,
    windSpeed DOUBLE,
    rainfall DOUBLE
);