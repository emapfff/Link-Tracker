CREATE TABLE IF NOT EXISTS consists
(
    tg_chat_id BIGINT NOT NULL,
    link_id    BIGINT NOT NULL,

    PRIMARY KEY (tg_chat_id, link_id),
    FOREIGN KEY (tg_chat_id) REFERENCES chat (tg_chat_id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES link (link_id) ON DELETE CASCADE
)
