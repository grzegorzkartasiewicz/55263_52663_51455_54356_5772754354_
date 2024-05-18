CREATE TABLE duel(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    participant1 VARCHAR(45),
    participant2 VARCHAR(45),
    winner Varchar(45),
    position INT,
    round INT
);