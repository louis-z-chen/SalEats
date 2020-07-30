DROP DATABASE if exists assignment4;
CREATE DATABASE assignment4;
Use assignment4;

CREATE TABLE UserAccounts (
	UserID int(30) primary key not null auto_increment,
    Email varchar(50) not null unique,
    Username varchar(50) not null,
    Password varchar(50) not null,
    isGoogleAccount varchar(50) not null
);

CREATE TABLE UserFavorites (
	RestaurantID int(30) primary key not null auto_increment,
    UserID int(30) not null,
    YelpID varchar(50) not null,
    RestaurantName varchar(50) not null,
	City varchar(100) not null,
    Country varchar(100) not null,
    State varchar(100) not null,
    Address1 varchar(100) not null,
    ZipCode varchar(100) not null,
	URL varchar(10000) not null,
    ImageURL varchar(10000) not null,
    PhoneNumber varchar(50) not null,
    Rating Decimal(15,10) not null,
    ReviewCount int(30) not null,
    Distance Decimal(15,10) not null,
    Cuisine varchar(50) not null,
    Price varchar(10) not null,
    Latitude Decimal(15,10) not null,
    Longitude Decimal(15,10) not null,
    FOREIGN KEY fk1(UserID) REFERENCES UserAccounts(UserID)
);
