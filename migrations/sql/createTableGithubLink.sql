CREATE TABLE IF NOT EXISTS github_links
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY,
    link_id BIGINT,
    count_branches INT,

    PRIMARY KEY (id),
    FOREIGN KEY (link_id) REFERENCES link (id) ON DELETE CASCADE
)
