
--Edit the actuators and groups
--Creates a new actuator
CREATE PROCEDURE NewActuator (_HouseID int, _Nickname varchar(32),
    _Address varchar(20), _Location varchar(64), Model varchar(64))
BEGIN
    INSERT INTO Actuators (HouseID, Nickname, Address, Location, model)
    VALUES (_HouseID, _Nickname, _Adress, _Location, _Model);
END; --Test: Pass

--Deletes an actuator and it's group belonging
CREATE PROCEDURE RmActuator (_ActID int)
BEGIN
    DELETE FROM GroupActuator
    WHERE ActID = _ActID;

    DELETE FROM Actuators
    WHERE ActID = _ActID;
END; --Test: Pass

--Create a new group
CREATE PROCEDURE NewGroup (_Nickname varchar(32), _HouseID int, _Descript varchar(255))
BEGIN
    INSERT INTO Groups (Nickname, HouseID, Descript)
    VALUES (_Nickname, _HouseID, _Descript);
END; --Test: Pass

--Removes a group and it's actuator linkages
CREATE PROCEDURE RmGroup (_GroupID int)
BEGIN
    DELETE FROM GroupActuator
    WHERE GroupID = _GroupID;

    DELETE FROM Groups
    WHERE GroupID = _GroupID;
END; --Test: Pass

--Add an existing actuator to an existing group
CREATE PROCEDURE AddGroupActuator (_GroupID int, _ActID int)
BEGIN
    INSERT INTO GroupActuator (GroupID, ActID)
    VALUES (_GroupID, _ActID);
END; --Test: Pass

--Remove an actuator from a group
CREATE PROCEDURE RmGroupActuator (_GroupID int, _ActID int)
BEGIN
    DELETE FROM GroupActuator 
    WHERE GroupID = _GroupID AND ActID = _ActID;
END; --Test: Pass



--See values and list adressable componants
--See actuators from a house
CREATE PROCEDURE HouseActuators (_HouseID int)
BEGIN
    SELECT ActID, Nickname, Address, Location, Model
    FROM Actuators
    WHERE HouseID = _HouseID;
END; --Test: Pass

--See actuators from all houses
CREATE PROCEDURE ShowAllActuators ()
BEGIN
    SELECT * FROM Actuators;
END; --Test: Pass

--See groups in a house
CREATE PROCEDURE HouseGroups (_HouseID int)
BEGIN
    SELECT GroupID, Nickname, Descript
    FROM Groups 
    WHERE HouseID = _HouseID;
END; --Test: Pass

--See groups and their members in a house
CREATE PROCEDURE HouseGroupActs (_HouseID int)
BEGIN
    SELECT Groups.GroupID, Groups.Nickname, Groups.Descript, 
        Actuators.ActID, Actuators.Nickname, Actuators.Location, Actuators.Model
    FROM Actuators
        INNER JOIN GroupActuator 
            ON Actuators.ActID = GroupActuator.ActID
        INNER JOIN Groups 
            ON GroupActuator.GroupID = Groups.GroupID
    WHERE Groups.HouseID = _HouseID AND Actuators.HouseID = _HouseID;
END; --Test: Pass

--See members of a group
CREATE PROCEDURE GroupMembers (_GroupID int)
BEGIN
    SELECT Actuators.ActID, Nickname, Address, Actuators.HouseID, Location, Model
    FROM Actuators
        INNER JOIN GroupActuator
            ON GroupActuator.ActID = Actuators.ActID 
    WHERE GroupActuator.GroupID = _GroupID;
END; --Test: Pass
