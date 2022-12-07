-- Active: 1670405054298@@localhost@3306@TestIoT
--Need to set the DELIMITER to // First.
DELIMITER //
--New User
CREATE PROCEDURE NewUser (_UserID varchar(32), _Password varchar(32))
BEGIN
    INSERT INTO Users (UserID, UserPass) 
    VALUES (_UserID, _Password);
END //


--User X Creates a house
CREATE PROCEDURE UserCreatesHouse (_UserID varchar(32), _Nickname varchar(32), _Category varchar(32))
BEGIN
    --Create new House
    INSERT INTO Houses (Nickname, Category) 
    VALUES (_Nickname, _Category);
    --Link new house to the user and grant owner status
    INSERT INTO UserHouse (UserID, HouseID, Permission)
    VALUES (_UserID, LAST_INSERT_ID(), 4);
END //


--Invites user to house
CREATE PROCEDURE InviteToHouse (_UserID varchar(32), _HouseID int, _Permission int)
BEGIN
    INSERT INTO UserHouse (UserID, HouseID, Permission) 
    VALUES (_UserID, _HouseID, _Permission);
END //


--Removes User from House
CREATE PROCEDURE RemoveHouseUser 
(_UserID varchar(32), _AdminID varchar(32), _HouseID int)
BEGIN
    DELETE FROM UserHouse
    WHERE UserID=_UserID AND HouseID=_HouseID AND _UserID!=_AdminID AND
    EXISTS (SELECT UserID
        FROM UserHouse 
        WHERE UserID = _AdminID AND Permission >= 3 AND HouseID = _HouseID);
END //


--Change User's permissions
CREATE PROCEDURE ChangeUserPerm 
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
END //

CREATE PROCEDURE TestLogin
(_UserID varchar(32), _Password varchar(32))
BEGIN
    SELECT COUNT(UserID) AS ValidAuth
    FROM Users
    WHERE UserID=_UserID AND UserPass=_Password;
END ;



--Procedures to check data
CREATE PROCEDURE UserHouses (_UserID varchar(32))
BEGIN
    SELECT Users.UserID, UserHouse.Permission, Houses.HouseID, Houses.Nickname, Houses.Category
    FROM Users
        INNER JOIN UserHouse
            ON Users.UserID = UserHouse.UserID
        INNER JOIN Houses
            ON UserHouse.HouseID = Houses.HouseID
    WHERE Users.UserID LIKE _UserID;
END ;

CREATE PROCEDURE HouseUsers (_HouseID int)
BEGIN
    SELECT Users.UserID, UserHouse.Permission, Houses.HouseID, Houses.Nickname, Houses.Category
    FROM Users
        INNER JOIN UserHouse
            ON Users.UserID = UserHouse.UserID
    WHERE UserHouse.HouseID = _HouseID;
END;

DELIMITER ;

