CREATE DATABASE IF NOT EXISTS cinema_db;
USE cinema_db;

CREATE TABLE IF NOT EXISTS seats (
  seat_id VARCHAR(5) PRIMARY KEY,
  status ENUM('available','booked') DEFAULT 'available'
);

INSERT INTO seats (seat_id) VALUES
('A1'),('A2'),('A3'),('A4'),('A5'),
('B1'),('B2'),('B3'),('B4'),('B5'),
('C1'),('C2'),('C3'),('C4'),('C5')
ON DUPLICATE KEY UPDATE seat_id = seat_id;
