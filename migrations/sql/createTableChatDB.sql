CREATE TABLE IF NOT EXISTS chat
(
    id   BIGINT    NOT NULL,
    userName TEXT      NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
)
