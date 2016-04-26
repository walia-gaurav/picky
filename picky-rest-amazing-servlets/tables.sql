DROP DATABASE picky;
CREATE DATABASE picky;
USE picky;

CREATE TABLE IF NOT EXISTS `User` (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    username varchar(64) NOT NULL UNIQUE,
    token varchar(64),
    password varchar(64) NOT NULL
) ENGINE = MyISAM;

INSERT INTO `User`(username, token, password) VALUES ('user', NULL, 'password');

CREATE TABLE IF NOT EXISTS Photo (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    url varchar(1024) NOT NULL UNIQUE
) ENGINE = MyISAM;

CREATE TABLE IF NOT EXISTS Location (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    UNIQUE(latitude, longitude)
) ENGINE = MyISAM;

CREATE TABLE IF NOT EXISTS Picky (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    title varchar(1024) NOT NULL,
    userId int(11) NOT NULL,
    leftPhotoId int(11) NOT NULL,
    rightPhotoId int(11) NOT NULL,
    locationId int(11) NOT NULL,
    leftVotes int(11),
    rightVotes int(11),
    expirationTime timestamp NOT NULL
) ENGINE = MyISAM;