CREATE TABLE IF NOT EXISTS apps
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    uri varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS stats
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app_id BIGINT NOT NULL,
    ip varchar(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_stats_apps FOREIGN KEY (app_id) REFERENCES apps(id) ON DELETE CASCADE
);