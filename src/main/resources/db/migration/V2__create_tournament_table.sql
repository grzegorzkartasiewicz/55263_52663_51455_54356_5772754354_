CREATE TABLE tournament
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    number_of_competitors INT                   NOT NULL,
    winner_id             BIGINT                NULL,
    tournament_name       VARCHAR(255)          NULL,
    tournament_data       VARCHAR(255)          NULL,
    category              VARCHAR(255)          NULL,
    email                 VARCHAR(255)          NULL,
    CONSTRAINT pk_tournament PRIMARY KEY (id)
);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_WINNER FOREIGN KEY (winner_id) REFERENCES competitor (id);

ALTER TABLE competitor
    ADD CONSTRAINT FK_COMPETITOR_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);
