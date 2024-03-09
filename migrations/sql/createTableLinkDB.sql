CREATE TABLE IF NOT EXISTS link
(
    link_id     BIGINT GENERATED ALWAYS AS IDENTITY,
    url         VARCHAR(500) NOT NULL,
    last_update TIMESTAMP    NOT NULL,
    PRIMARY KEY (link_id)
)
