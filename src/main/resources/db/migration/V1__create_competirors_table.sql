CREATE TABLE competitor
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    surname       VARCHAR(255)          NOT NULL,
    age           INT                   NOT NULL,
    category      VARCHAR(255)          NULL,
    club          VARCHAR(255)          NOT NULL,
    skipped_last  BIT(1)                NOT NULL,
    tournament_id BIGINT                NULL,
    CONSTRAINT pk_competitor PRIMARY KEY (id)
);