-- former site
CREATE DATABASE IF NOT EXISTS ShoppingExpress_v_0_7;

USE ShoppingExpress_v_0_7;

DROP TABLE IF EXISTS Users;
CREATE TABLE IF NOT EXISTS Users (
	UserId INT NOT NULL AUTO_INCREMENT,
	Login VARCHAR(255) NOT NULL,
	Password CHAR(64) NOT NULL,
	Description TEXT,
	PRIMARY KEY (UserId)
);

DROP TABLE IF EXISTS Products;
CREATE TABLE IF NOT EXISTS Products (
	ProductId INT NOT NULL AUTO_INCREMENT,
	Name VARCHAR(255) NOT NULL,
	Description TEXT,
	Price FLOAT NOT NULL,
	QuantityAvailable INT NOT NULL DEFAULT 0,
	PRIMARY KEY (ProductId)
);

-- theophile0910
INSERT INTO Users(Login, Password, Description) VALUES ('admin', 'theophile0910', 'Papa depuis 2010, j\'ai un fils aîné qui s\'appelle Théophile et une fille qui s\'appelle Marcelline');
-- radiation
INSERT INTO Users(Login, Password, Description) VALUES ('lucie', 'radiation', 'Je m\'appelle Lucie et j\'aime les tapis');
-- radiateur
INSERT INTO Users(Login, Password, Description) VALUES ('raoul', 'radiateur', 'Je m\'appelle Raoul et j\'aime les poules');


INSERT INTO Products(Name, Description, Price, QuantityAvailable) VALUES ('Hat', 'A nice little hat.', 15.49, 4);
INSERT INTO Products(Name, Description, Price) VALUES ('Spiral of power', 'A spiral to bring you power.', 99.99);
INSERT INTO Products(Name, Description, Price, QuantityAvailable) VALUES ('Green mint', 'A wonderful decorative mint leaf.', 0.0, 15);
