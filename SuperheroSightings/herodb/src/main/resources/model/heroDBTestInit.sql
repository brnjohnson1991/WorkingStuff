DROP DATABASE IF EXISTS herodbtest;
CREATE DATABASE herodbtest;
USE herodbtest;

CREATE TABLE `super` (
	id INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(30) NOT NULL,
    `description` VARCHAR(140),
    isvillain BOOLEAN NOT NULL DEFAULT FALSE,
    powerid INT NOT NULL
);

CREATE TABLE `power` (
	id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    `description` VARCHAR(140)
);

CREATE TABLE super_organization (
	superId INT NOT NULL,
    organizationid INT NOT NULL
);

CREATE TABLE `organization` (
	id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    `description` VARCHAR(140),
    addressid INT NOT NULL
); 

CREATE TABLE address (
	id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    `description` VARCHAR(140) NOT NULL,
    streetaddress VARCHAR(50),
    territory VARCHAR(30),
    country VARCHAR(30) NOT NULL,
    postalcode VARCHAR(10),
    latitude DECIMAL(8, 6),
    longitude DECIMAL(9, 6),
    city VARCHAR(30)
);

CREATE TABLE sighting (
	id INT PRIMARY KEY AUTO_INCREMENT,
    addressid INT NOT NULL,
    `date` DATE NOT NULL,
    `description` VARCHAR(140)
);

CREATE TABLE super_sighting (
	superid INT,
    sightingid INT
);

ALTER TABLE super_organization
    ADD CONSTRAINT fk_superorganization_super
        FOREIGN KEY (superid)
        REFERENCES `super` (id),
	ADD CONSTRAINT fk_superorganization_organization
		FOREIGN KEY (organizationid)
		REFERENCES `organization` (id);
        
ALTER TABLE `organization`
	ADD CONSTRAINT fk_organization_address
		FOREIGN KEY (addressid)
        REFERENCES address (id);
        
ALTER TABLE sighting
	ADD CONSTRAINT fk_sighting_address
		FOREIGN KEY (addressid)
		REFERENCES address (id);
        
ALTER TABLE super_sighting
	ADD CONSTRAINT fk_supersighting_super
		FOREIGN KEY (superid)
        REFERENCES `super` (id),
	ADD CONSTRAINT fk_supersighting_sighting
		FOREIGN KEY (sightingid)
		REFERENCES sighting (id);
        
ALTER TABLE `super`
	ADD CONSTRAINT fk_super_power
		FOREIGN KEY (powerid)
        REFERENCES power (id);