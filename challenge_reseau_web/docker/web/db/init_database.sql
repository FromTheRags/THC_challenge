CREATE DATABASE PictureParadise;
CREATE USER web@localhost IDENTIFIED BY 'spartanswhatisyourprofession?motdepasse!!!moo***ootdepasse!aouhaouh!!';
GRANT ALL ON PictureParadise.* TO web@localhost;
FLUSH PRIVILEGES;
USE PictureParadise;

CREATE TABLE Users (
	UserId INT NOT NULL AUTO_INCREMENT,
	Login VARCHAR(255) NOT NULL,
	Password VARCHAR(255) NOT NULL,
	Description TEXT NOT NULL,
	PRIMARY KEY (UserId)
);
INSERT INTO Users(Login, Password, Description) VALUES ('admin', 'Pa$$w0rd', 'I am the web site administrator');
INSERT INTO Users(Login, Password, Description) VALUES ('tom', 'jerry', 'I hate mouses');
INSERT INTO Users(Login, Password, Description) VALUES ('jerry', 'tom', 'I hate cats');

CREATE TABLE Friends (
	FriendshipId INT NOT NULL AUTO_INCREMENT,
	User1Id INT NOT NULL,
	User2Id INT NOT NULL,
	PRIMARY KEY (FriendshipId)
);
INSERT INTO Friends(User1Id, User2Id) VALUES (2, 3);
INSERT INTO Friends(User1Id, User2Id) VALUES (1, 2);
INSERT INTO Friends(User1Id, User2Id) VALUES (1, 3);

CREATE TABLE Pictures (
	PictureId INT NOT NULL AUTO_INCREMENT,
	FileName VARCHAR(255) NOT NULL,
	Title VARCHAR(255) NOT NULL,
	Description TEXT NOT NULL,
	`Key` BINARY(32) NULL,
	`DateTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UserId INT NOT NULL,
	PRIMARY KEY (PictureId)
);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('bliss.jpg', 'Memories...', 'Do you remember that screen ?', '2021-02-15 23:57:23', 1);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('forest.jpg', 'Forest', '', '2021-01-02 14:23:37', 1);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('sea.jpg', 'Sea', '', '2020-01-01 15:28:46', 1);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('valley.jpg', 'Valley', '', '2019-12-29 17:56:04', 1);
INSERT INTO Pictures(FileName, Title, Description, `Key`, `DateTime`, UserId) VALUES ('attack.jpg', 'Attack', '', UNHEX('1C4C41F5E76C6F798FDAD57634F7DA616AE16FB9858FDA6910DE0E0208307376'), '2021-03-02 18:46:53', 2);
INSERT INTO Pictures(FileName, Title, Description, `Key`, `DateTime`, UserId) VALUES ('racket.jpg', 'Racket', '', UNHEX('B47A4F3FF28124BBD6E08B1411D71E83535233F9A10D29BEFE7F424194F17138'), '2021-03-05 19:24:15', 2);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('dancing.jpg', 'Dancing', '', '2019-10-27 11:48:51', 2);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('friendship.png', 'Friendship', '', '2019-09-30 13:29:26', 2);
INSERT INTO Pictures(FileName, Title, Description, `Key`, `DateTime`, UserId) VALUES ('fight.jpg', 'Fight', '', UNHEX('8569F7473514796D8831243BC38E022356795ABDB7EAA81BB8EF29DB0C1D54DF'), '2021-03-01 23:59:59', 3);
INSERT INTO Pictures(FileName, Title, Description, `Key`, `DateTime`, UserId) VALUES ('try.jpg', 'Try', '', UNHEX('A131BCC3887CE2999C6325E81FC5B45E7FFE2612B815BEB2894BB1F07650A725'), '2020-12-25 11:27:51', 3);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('mock.png', 'Mock', '', '2020-11-25 22:14:28', 3);
INSERT INTO Pictures(FileName, Title, Description, `DateTime`, UserId) VALUES ('rest.png', 'Rest', '', '2020-11-25 22:15:13', 3);

