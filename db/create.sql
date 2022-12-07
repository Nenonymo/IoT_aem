CREATE TABLE Users (
    UserID varchar(32) NOT NULL,
    UserPass varchar(32) NOT NULL,
    PRIMARY KEY (UserID)
);

CREATE TABLE Houses (
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
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (HouseID) REFERENCES Houses(HouseID)
);

CREATE TABLE Actuators (
    ActID int NOT NULL AUTO_INCREMENT, --Primary key
    Nickname varchar(32) NOT NULL,
    Addr varchar(16) NOT NULL,
    HouseID int NOT NULL, --Forei
    Loc varchar(64),
    Model varchar(64),
    PRIMARY KEY (ActID),
    FOREIGN KEY (HouseID) REFERENCES Houses(HouseID)
);

CREATE TABLE Groups (
    GroupID int NOT NULL AUTO_INCREMENT, --Primary key
    Nickname varchar(32) NOT NULL,
    HouseID int NOT NULL, --Foreign key
    Descript varchar(255),
    PRIMARY KEY (GroupID),
    FOREIGN KEY (HouseID) REFERENCES Houses(HouseID)
);

CREATE TABLE GroupActuator (
    GroupID int NOT NULL, --Primary*Foreign key: Group ID from Groups
    ActID int NOT NULL, --Primary*Foreign key: ActID from Actuators
    PRIMARY KEY (GroupID, ActId),
    FOREIGN KEY (GroupID) REFERENCES Groups(GroupID),
    FOREIGN KEY (ActID) REFERENCES Actuators(ActID)
);