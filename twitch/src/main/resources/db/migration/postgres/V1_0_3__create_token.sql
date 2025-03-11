CREATE TYPE token_type AS ENUM ('AccessToken', 'RefreshToken');
CREATE CAST (token_type AS TEXT) WITH INOUT AS IMPLICIT;
CREATE CAST (VARCHAR AS token_type) with inout as IMPLICIT;


CREATE TABLE TOKEN
(
    id         SERIAL PRIMARY KEY,
    token      VARCHAR(255) NOT NULL,
    token_type token_type   NOT NULL,
    expired    BOOLEAN      NOT NULL,
    revoked    BOOLEAN      NOT NULL,
    user_id    INTEGER      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USER_COMMON_DATA (id)

);