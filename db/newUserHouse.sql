-- Active: 1670505946415@@109.234.160.193@3306@ahfr0859_IoTProject
--Need to set the DELIMITER to // First.

--New User
CREATE PROCEDURE NewUser (_UserID varchar(32), _Password varchar(32))
BEGIN
    INSERT INTO User (UserID, UserPass) 
    VALUES (_UserID, _Password);
END ; --Test: Pass


--User X Creates a house
CREATE PROCEDURE NewHouseByUser (_UserID varchar(32), _Nickname varchar(32), _Category varchar(32))
BEGIN
    --Create new House
    INSERT INTO House (Nickname, Category) 
    VALUES (_Nickname, _Category);
    --Link new house to the user and grant owner status
    INSERT INTO UserHouse (UserID, HouseID, Permission)
    VALUES (_UserID, LAST_INSERT_ID(), 4);
END ; --Test: Pass

--Delete House and everything linked to it
CREATE PROCEDURE RemoveHouse (_HouseID int)
BEGIN
    --Delete all User-House links
    DELETE FROM UserHouse WHERE HouseID=_HouseID;
    --Delete all group-actuator links
    DELETE FROM GroupActuator WHERE GroupID IN (SELECT GroupID FROM Groups WHERE HouseID=_HouseID);
    --Delete all actuators
    DELETE FROM Actuator WHERE HouseID=_HouseID;
    --Delete all groups
    DELETE FROM Groups WHERE HouseID=_HouseID;
    --Delete House
    DELETE FROM House WHERE HouseID=_HouseID;
END; --Test: Untested

--Invites user to house
CREATE PROCEDURE AddUserToHouse (_UserID varchar(32), _HouseID int, _Permission int)
BEGIN
    INSERT INTO UserHouse (UserID, HouseID, Permission) 
    VALUES (_UserID, _HouseID, _Permission);
END ; --Test: Pass


--Removes User from House
CREATE PROCEDURE RemoveHouseUser 
(_UserID varchar(32), _AdminID varchar(32), _HouseID int)
BEGIN
    DELETE FROM UserHouse
    WHERE UserID=_UserID AND HouseID=_HouseID AND _UserID!=_AdminID AND
    EXISTS (SELECT UserID
        FROM UserHouse 
        WHERE UserID = _AdminID AND Permission >= 3 AND HouseID = _HouseID);
END ; --Test: Pass


--Change User's permissions
CREATE PROCEDURE ChangeUserPermissions 
(_UserID varchar(32), _AdminID varchar(32), _HouseID int, _Permission int)
BEGIN
    UPDATE UserHouse
    SET Permission=_Permission
    WHERE EXISTS (SELECT UserID 
        FROM UserHouse 
        WHERE UserID = _AdminID 
        AND HouseID = _HouseID
        AND Permission >= 3)
    AND _Permission <= (SELECT Permission 
        FROM UserHouse 
        WHERE UserID=_AdminID 
        AND HouseID=_HouseID);
END ; --Test: Pass


--Changes User's Password
CREATE PROCEDURE ChangeUserPassword
    (_UserID varchar(32), _Password varchar(32))
BEGIN
    UPDATE User
    SET UserPass = _Password
    WHERE UserID = _UserID;
END; --Test: Pass


--Test the User authentification (1=Valid, 0=Invalid:={temperature:.3f}outVal)
CREATE PROCEDURE TestLogin 
    (_UserID varchar(32), _Password varchar(32))
BEGIN
    SELECT COUNT(UserID) AS ValidAuth
    FROM User
    WHERE UserID=_UserID AND UserPass=_Password;
END ; --Test: Pass


--Test if the user exists
CREATE PROCEDURE TestUser (_UserID varchar(32))
BEGIN
    SELECT COUNT(UserID) AS ValidUser
    FROM User
    WHERE UserID=_UserID;
END;

--Test: Pass
CALL TestUser ('user1');


--Procedures to check data
CREATE PROCEDURE GetUserHouses (_UserID varchar(32))
BEGIN
    SELECT UserHouse.Permission, House.HouseID, House.Nickname, House.Category
    FROM UserHouse
        INNER JOIN House
            ON UserHouse.HouseID = House.HouseID
    WHERE UserHouse.UserID LIKE _UserID;
END ; 
--Test: Pass
CALL GetUserHouses ('harryp');


--Returns a list of all user with access to a specific house
CREATE PROCEDURE GetHouseUsers (_HouseID int)
BEGIN
    SELECT UserID, Permission
    FROM UserHouse
    WHERE HouseID = _HouseID;
END ; 
--Test: Pass
CALL GetHouseUsers (18);
