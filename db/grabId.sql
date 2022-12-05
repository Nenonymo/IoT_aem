--Grab Tellnet ID from a Group ID
CREATE PROCEDURE GetGroupAdresses @GroupID int
AS
    SELECT TellID
    FROM Actuators
    WHERE ActID = (SELECT ActID FROM GroupActuator WHERE GroupID = @GroupID)
GO;

--Grab Tellnet ID from a Actuator ID
CREATE PROCEDURE GetActAddress @ActID
AS
    SELECT TellID FROM Actuators WHERE ActID = @ActID;
GO;