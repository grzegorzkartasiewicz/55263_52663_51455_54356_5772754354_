CREATE TABLE competitors (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  surname VARCHAR(45) NOT NULL,
  age INT NOT NULL CHECK (age >= 4 AND age <= 100),
  gender CHAR(1),  -- Assuming gender is stored as a single character, e.g., 'M' or 'F'
  competition VARCHAR(100),
  club VARCHAR(100) NOT NULL,
  advancement VARCHAR(100),
  weight DECIMAL(5, 2),
  skippedLast BOOLEAN
);