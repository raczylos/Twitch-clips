CREATE TYPE user_type AS ENUM ('TwitchUser', 'User');
CREATE CAST (user_type AS TEXT) WITH INOUT AS IMPLICIT;
CREATE CAST (VARCHAR AS user_type) with inout as IMPLICIT;

CREATE TYPE user_role AS ENUM ('User', 'Admin');
CREATE CAST (user_role AS TEXT) WITH INOUT AS IMPLICIT;
CREATE CAST (VARCHAR AS user_role) with inout as IMPLICIT;

CREATE SEQUENCE user_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE USER_COMMON_DATA
(
    id        SERIAL PRIMARY KEY,
    email     VARCHAR(255) UNIQUE NOT NULL,
    login     VARCHAR(255) UNIQUE NOT NULL,
    role      user_role           NOT NULL,
    user_type user_type           NOT NULL
);

CREATE TABLE USER_
(
    id       SERIAL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES USER_COMMON_DATA (id)
);

CREATE TABLE TWITCH_USER
(
    id        SERIAL PRIMARY KEY,
    twitch_id VARCHAR(255) UNIQUE NOT NULL,
    FOREIGN KEY (id) REFERENCES USER_COMMON_DATA (id)
);