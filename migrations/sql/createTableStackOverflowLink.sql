CREATE TABLE IF NOT EXISTS stackoverflow_link
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY,
    link_id BIGINT,
    answer_count INT,

    PRIMARY KEY (id),
    FOREIGN KEY (link_id) REFERENCES link (id) ON DELETE CASCADE
)
