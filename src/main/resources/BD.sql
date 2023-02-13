DROP DATABASE IF EXISTS jwt;
CREATE DATABASE jwt;
Use jwt;

CREATE TABLE users(
                      nombre varchar(16) not null primary key,
                      pwd varchar(30) not null);

INSERT INTO users(nombre,pwd) VALUES ('juan','juan'),('admin','admin'),('p','p'),('f','f');
