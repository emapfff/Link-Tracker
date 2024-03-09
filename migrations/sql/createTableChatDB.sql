CREATE TABLE IF NOT EXISTS chat
(
    tg_chat_id   BIGINT    NOT NULL,
    tg_chat_name TEXT      NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    PRIMARY KEY (tg_chat_id)
)
