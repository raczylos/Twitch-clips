CREATE TABLE STREAMER
(
    id                SERIAL PRIMARY KEY,
    login             VARCHAR(255) UNIQUE NOT NULL,
    display_name      VARCHAR(255) UNIQUE NOT NULL,
    twitch_id         VARCHAR(255) UNIQUE NOT NULL,
    profile_image_url VARCHAR(255)
);