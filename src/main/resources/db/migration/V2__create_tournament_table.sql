CREATE TABLE tournament(
    id BIGINT NOT NULL,
    number_of_competitors INT    NOT NULL,
    winner_id BIGINT NULL,
    CONSTRAINT pk_tournament PRIMARY KEY (id)
);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_WINNER FOREIGN KEY (winner_id) REFERENCES competitor (id);
