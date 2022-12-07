--Returns a list of all adressable actuators in a house
CREATE PROCEDURE GetActuators (_HouseID int)
BEGIN
    SELECT ActID, Nickname, Loc, Model
    FROM Actuators
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = _HouseID);
END;

--Returns a list of all adressable groups in a house
CREATE PROCEDURE GetGroups (_HouseID int)
BEGIN
    SELECT GroupID, Nickname, Descript
    FROM Groups
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = _HouseID);
END;

--Returns a list of all adressable entities in a house
CREATE PROCEDURE GetEntites (_HouseID int)
BEGIN
    SELECT "Actuator" AS Category, ActID AS ID, Nickname,
    FROM Actuators
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = _HouseID)
    UNION ALL
    SELECT "Group" AS Category, GroupID AS ID, Nickname,
    FROM Groups
    WHERE HouseID = (SELECT HouseID FROM Houses WHERE HouseID = _HouseID)
END;
