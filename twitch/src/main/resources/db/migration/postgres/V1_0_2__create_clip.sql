CREATE TABLE CLIP
(
    id               SERIAL PRIMARY KEY,
    twitch_clip_id   VARCHAR(255) UNIQUE NOT NULL,
    url              VARCHAR(255)        NOT NULL,
    embed_url        VARCHAR(255)        NOT NULL,
    broadcaster_id   VARCHAR(255)        NOT NULL,
    broadcaster_name VARCHAR(255)        NOT NULL,
    creator_id       VARCHAR(255)        NOT NULL,
    creator_name     VARCHAR(255)        NOT NULL,
    video_id         VARCHAR(255),
    game_id          VARCHAR(255),
    language         VARCHAR(255),
    clip_title       VARCHAR(255)        NOT NULL,
    view_count       INTEGER             NOT NULL,
    created_at       VARCHAR(255),
    thumbnail_url    VARCHAR(255)        NOT NULL,
    clip_duration    FLOAT               NOT NULL,
    vod_offset       INTEGER,
    streamer_id      INTEGER             NOT NULL,
    FOREIGN KEY (streamer_id) REFERENCES STREAMER (id)
);