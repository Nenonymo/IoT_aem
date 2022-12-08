-- Active: 1670505946415@@109.234.160.193@3306@ahfr0859_IoTProject
--Returns a list of all adressable Actuator in a house
CREATE PROCEDURE GetActuator (_HouseID int)
BEGIN
    SELECT ActID, Nickname, Loc, Model
    FROM Actuator
    WHERE HouseID = (SELECT HouseID FROM House WHERE HouseID = _HouseID);
END; --Test: Pass

--Returns a list of all adressable groups in a house
CREATE PROCEDURE GetGroups (_HouseID int)
BEGIN
    SELECT GroupID, Nickname, Description
    FROM Groups
    WHERE HouseID = _HouseID;
END; --Test: Pass

--Returns a list of all adressable entities in a house
CREATE PROCEDURE GetEntites (_HouseID int)
BEGIN
    SELECT 'Actuator' AS Category, ActID AS ID, Nickname
    FROM Actuator
    WHERE HouseID = _HouseID
    UNION ALL
    SELECT 'Group' AS Category, GroupID AS ID, Nickname
    FROM Groups
    WHERE HouseID = _HouseID;
END; --Test: Pass
