--Returns a list of all adressable actuators in a house
CREATE PROCEDURE GetActuators @HouseID int
AS
    SELECT ActID, Nickname, Loc, Model
    FROM Actuators
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = @HID);
GO;

--Returns a list of all adressable groups in a house
CREATE PROCEDURE GetGroups @HouseID int
AS
    SELECT GroupID, Nickname, Descript
    FROM Groups
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = @HID);
GO;

--Returns a list of all adressable entities in a house
CREATE PROCEDURE GetEntites @HouseID int
AS
    SELECT "Actuator" AS Category, ActID AS ID, Nickname,
    FROM Actuators
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = @HID)
    UNION ALL
    SELECT "Group" AS Category, GroupID AS ID, Nickname,
    FROM Groups
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = @HID)
GO;
