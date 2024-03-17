CREATE TABLE IF NOT EXISTS chat
(
    id   BIGINT    NOT NULL,
    userName TEXT      NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
)
