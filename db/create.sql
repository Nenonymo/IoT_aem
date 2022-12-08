-- Active: 1670505946415@@109.234.160.193@3306@ahfr0859_IoTProject
CREATE TABLE User (
    UserID varchar(32) NOT NULL,
    UserPass varchar(32) NOT NULL,
    PRIMARY KEY (UserID)
);

CREATE TABLE House (
    HouseID int NOT NULL AUTO_INCREMENT,
    Nickname varchar(32),
    Category varchar(32),
    PRIMARY KEY (HouseID)
);

CREATE TABLE UserHouse (
    UserID varchar(32) NOT NULL, --Primary*foreign
    HouseID int NOT NULL, --Primary*foreign
    Permission int NOT NULL DEFAULT(0), --0=Watch only, 1=Interact, 3=Admin, 4=Owner
    PRIMARY KEY (UserID, HouseID),
    FOREIGN KEY (UserID) REFERENCES User(UserID),
    FOREIGN KEY (HouseID) REFERENCES House(HouseID)
);

CREATE TABLE Actuator (
    ActID int NOT NULL AUTO_INCREMENT, --Primary key
    Nickname varchar(32) NOT NULL,
    Address varchar(16) NOT NULL,
    HouseID int NOT NULL, --Forei
    Location varchar(64),
    Model varchar(64),
    PRIMARY KEY (ActID),
    FOREIGN KEY (HouseID) REFERENCES House(HouseID)
);

CREATE TABLE Groups (
    GroupID int NOT NULL AUTO_INCREMENT, --Primary key
    Nickname varchar(32) NOT NULL,
    HouseID int NOT NULL, --Foreign key
    Description varchar(255),
    PRIMARY KEY (GroupID),
    FOREIGN KEY (HouseID) REFERENCES House(HouseID)
);

CREATE TABLE GroupActuator (
    GroupID int NOT NULL, --Primary*Foreign key: Group ID from Groups
    ActID int NOT NULL, --Primary*Foreign key: ActID from Actuator
    PRIMARY KEY (GroupID, ActId),
    FOREIGN KEY (GroupID) REFERENCES Groups(GroupID),
    FOREIGN KEY (ActID) REFERENCES Actuator(ActID)
);