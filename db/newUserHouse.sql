--New User
CREATE PROCEDURE CreateUser @UID varchar(32), @UserPass binary(64)
AS
    INSERT INTO Users (UserID, UserPass) VALUES (@UID, @UserPass)
GO;

--User X Creates a house
CREATE PROCEDURE UserCreatesHouse @UID varchar(32), @Nickname varchar(32), @Category varchar(32)
AS
BEGIN
    INSERT INTO Houses (Nickname, Category) 
    VALUES (@Nickname, @Category)

    INSERT INTO UserHouse (UserID, HouseID, Permission)
    VALUES (@UID, SCOPE_IDENTITY(), 4)
END
GO;

--Invites user to house
CREATE PROCEDURE InviteToHouse @UID varchar(32), @HID int, @Perm int
AS
    INSERT INTO UserHouse (UserID, HouseID, Permission)
    VALUES (@UID, @HID, @Perm)
GO;

--Removes User from House
CREATE PROCEDURE RemoveHouseUser @UID varchar(32), @AID varchar(32), @HID int
AS
    DELETE FROM UserHouse
    WHERE UserID=@UID AND HouseID=@HID AND @UID!=@AID
    EXISTS (SELECT UserID --Verify that the user has the right to delete users
        FROM UserHouse 
        WHERE UserID = @AID AND Permission >= 3)
GO;

--Change User's permissions
CREATE PROCEDURE ChangeUserPerm @UID varchar(32), @AID varchar(32), @HID int, @Perm int
AS
    UPDATE UserHouse
    SET Permission=@Perm
    WHERE @Perm <= (SELECT Permission FROM UserHouse WHERE UserID=@UID AND HouseID=@HID)
    AND EXISTS (SELECT UserID --Verify that the user has the right to delete users
        FROM UserHouse 
        WHERE UserID = @AID AND Permission >= 3)
GO;

