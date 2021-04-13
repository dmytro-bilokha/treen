CREATE TABLE user_data
( id BIGINT NOT NULL AUTO_INCREMENT
, login VARCHAR(120) NOT NULL
, password_hash VARBINARY(128) NOT NULL
, password_salt VARBINARY(128) NOT NULL
, CONSTRAINT user_data_pk PRIMARY KEY (id)
, CONSTRAINT user_data_login_uq UNIQUE (login)
);
