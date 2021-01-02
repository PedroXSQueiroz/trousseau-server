CREATE TABLE trousseau_user(
    id_trousseau_user SERIAL NOT NULL PRIMARY KEY,
    user_name VARCHAR(256) NOT NULL,
    user_email VARCHAR(256) NOT NULL,
    user_password_hash BYTEA NOT NULL
);

