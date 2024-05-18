CREATE TABLE tournament(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    numbersOfCompetitors INT,
    winner BIGINT,
    FOREIGN KEY (winner_id) REFERENCES Competitor(id)
);