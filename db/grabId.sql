--Grab Tellnet ID from a Group ID
CREATE PROCEDURE GetGroupAddresses (_GroupID int)
BEGIN
    SELECT Address
    FROM Actuator
        INNER JOIN GroupActuator
            ON Actuator.ActID = GroupActuator.ActID
    WHERE GroupID = _GroupID;
END ; --Test: Pass

--Grab Tellnet ID from a Actuator ID
CREATE PROCEDURE GetActAddress (_ActuatorID int)
BEGIN
    SELECT Address FROM Actuator WHERE ActID = _ActuatorID;
END; --Test: Pass