CREATE TABLE FOLLOWER
(
    id             SERIAL PRIMARY KEY,
    twitch_user_id INTEGER NOT NULL,
    streamer_id    INTEGER NOT NULL,
    FOREIGN KEY (twitch_user_id) REFERENCES TWITCH_USER (id),
    FOREIGN KEY (streamer_id) REFERENCES STREAMER (id)

);