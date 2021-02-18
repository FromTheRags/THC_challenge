-- current site
CREATE DATABASE IF NOT EXISTS ShoppingExpress;

USE ShoppingExpress;

DROP TABLE IF EXISTS Users;
CREATE TABLE IF NOT EXISTS Users (
	UserId INT NOT NULL AUTO_INCREMENT,
	Login VARCHAR(255) NOT NULL,
	PasswordHash CHAR(64) NOT NULL,
	Salt CHAR(16) NOT NULL,
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
INSERT INTO Users(Login, PasswordHash, Salt, Description) VALUES ('admin', 'e1e270b5c806fd12be63fcd7a724fa7e80c3ae66e9a6d3e002817019a27e8be4', 'saltsaltsaltsalt', 'Papa depuis 2010, j\'ai un fils aîné qui s\'appelle Théophile, une fille qui s\'appelle Marcelline et un depuis peu un fils qui s\'appelle Augustin');
-- radiation
INSERT INTO Users(Login, PasswordHash, Salt, Description) VALUES ('lucie', '917e970e91c558aa9ca870cc80a80dbd239972ec8b9a589836002c650496d750', 'pepperpeppersalt', 'Je m\'appelle Lucie et j\'aime les tapis');
-- radiateur
INSERT INTO Users(Login, PasswordHash, Salt, Description) VALUES ('raoul', '61f368d5cc6669bcaf8c92c871a18338e4253ae3805fd6df9db70df59b8aeab2', 'cannellecannelle', 'Je m\'appelle Raoul et j\'aime les poules');
-- radariser
INSERT INTO Users(Login, PasswordHash, Salt, Description) VALUES ('jules', '0e05483a5fb756233a040935bb9d7628058864b4e9eaa73fe6135ee9d095ab30', 'sugarsugarsugars', 'Je m\'appelle Jules et j\'habite à Tulle');


INSERT INTO Products(Name, Description, Price, QuantityAvailable) VALUES ('Hat', 'A nice little hat.', 15.49, 4);
INSERT INTO Products(Name, Description, Price) VALUES ('Spiral of power', 'A spiral to bring you power.', 99.99);
INSERT INTO Products(Name, Description, Price, QuantityAvailable) VALUES ('Green mint', 'A wonderful decorative mint leaf.', 0.0, 15);
INSERT INTO Products(Name, Description, Price, QuantityAvailable) VALUES ('Flag of victory', 'Workers of all lands, unite !<br /><i style="color: green">Note : This article is dematerialized, you can download it instantly after purchasing it</i>', 999.99, 1);


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
