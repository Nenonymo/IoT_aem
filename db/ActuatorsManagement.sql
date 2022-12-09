
--Edit the Actuator and groups
--Creates a new actuator
CREATE PROCEDURE NewActuator (_HouseID int, _Nickname varchar(32),
    _Address varchar(20), _Location varchar(64), Model varchar(64))
BEGIN
    INSERT INTO Actuator (HouseID, Nickname, Address, Location, model)
    VALUES (_HouseID, _Nickname, _Adress, _Location, _Model);
END; --Test: Pass

--Deletes an actuator and it's group belonging
CREATE PROCEDURE RemoveActuator (_ActID int)
BEGIN
    DELETE FROM GroupActuator
    WHERE ActID = _ActID;

    DELETE FROM Actuator
    WHERE ActID = _ActID;
END; --Test: Pass

--Create a new group
CREATE PROCEDURE NewGroup (_Nickname varchar(32), _HouseID int, _Description varchar(255))
BEGIN
    INSERT INTO Groups (Nickname, HouseID, Description)
    VALUES (_Nickname, _HouseID, _Description);
END; --Test: Pass

--Removes a group and it's actuator linkages
CREATE PROCEDURE RemoveGroup (_GroupID int)
BEGIN
    DELETE FROM GroupActuator
    WHERE GroupID = _GroupID;

    DELETE FROM Groups
    WHERE GroupID = _GroupID;
END; --Test: Pass

--Add an existing actuator to an existing group
CREATE PROCEDURE AddActuatorToGroup (_GroupID int, _ActID int)
BEGIN
    INSERT INTO GroupActuator (GroupID, ActID)
    VALUES (_GroupID, _ActID);
END; --Test: Pass

--Remove an actuator from a group
CREATE PROCEDURE RemoveActuatorFromGroup (_GroupID int, _ActID int)
BEGIN
    DELETE FROM GroupActuator 
    WHERE GroupID = _GroupID AND ActID = _ActID;
END; --Test: Pass



--See values and list adressable componants
--See Actuator from a house
CREATE PROCEDURE GetHouseActuators (_HouseID int)
BEGIN
    SELECT ActID, Nickname, Address, Location, Model
    FROM Actuator
    WHERE HouseID = _HouseID;
END; --Test: Pass

--See Actuator from all House
CREATE PROCEDURE GetAllActuators ()
BEGIN
    SELECT * FROM Actuator;
END; --Test: Pass

--See groups in a house
CREATE PROCEDURE GetHouseGroups (_HouseID int)
BEGIN
    SELECT GroupID, Nickname, Description
    FROM Groups 
    WHERE HouseID = _HouseID;
END; --Test: Pass

--See groups and their members in a house
CREATE PROCEDURE GetHouseGroupsActuators (_HouseID int)
BEGIN
    SELECT Groups.GroupID, Groups.Nickname, Groups.Description, 
        Actuator.ActID, Actuator.Nickname, Actuator.Location, Actuator.Model
    FROM Actuator
        INNER JOIN GroupActuator 
            ON Actuator.ActID = GroupActuator.ActID
        INNER JOIN Groups 
            ON GroupActuator.GroupID = Groups.GroupID
    WHERE Groups.HouseID = _HouseID AND Actuator.HouseID = _HouseID;
END; --Test: Pass

--See members of a group
CREATE PROCEDURE GetGroupMembers (_GroupID int)
BEGIN
    SELECT Actuator.ActID, Nickname, Address, Actuator.HouseID, Location, Model
    FROM Actuator
        INNER JOIN GroupActuator
            ON GroupActuator.ActID = Actuator.ActID 
    WHERE GroupActuator.GroupID = _GroupID;
END; --Test: Pass
