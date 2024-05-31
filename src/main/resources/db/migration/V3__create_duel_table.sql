CREATE TABLE duel
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    participant1  BIGINT                NULL,
    participant2  BIGINT                NULL,
    winner        BIGINT                NULL,
    position      INT                   NOT NULL,
    round         INT                   NOT NULL,
    branch        VARCHAR(255)          NULL,
    tournament_id BIGINT                NULL,
    CONSTRAINT pk_duel PRIMARY KEY (id)
);

ALTER TABLE duel
    ADD CONSTRAINT FK_DUEL_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);