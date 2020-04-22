DROP DATABASE IF EXISTS mastermindtest;
CREATE DATABASE IF NOT EXISTS mastermindtest;
USE mastermindtest;

CREATE TABLE game (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    solution CHAR(4) NOT NULL,
    complete BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE round (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    guess CHAR(4) NOT NULL,
    exactMatches TINYINT,
    partialMatches TINYINT,
    `timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gameId INTEGER NOT NULL
);

ALTER TABLE round
	ADD CONSTRAINT fk_game
		FOREIGN KEY (gameId)
		REFERENCES game (id);
        
SELECT * FROM game;