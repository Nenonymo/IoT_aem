--Grab Tellnet ID from a Group ID
CREATE PROCEDURE GetGroupAdresses (_GroupID int)
BEGIN
    SELECT TellID
    FROM Actuators
    WHERE ActID = (SELECT ActID FROM GroupActuator WHERE GroupID = _GroupID);
END ;

--Grab Tellnet ID from a Actuator ID
CREATE PROCEDURE GetActAddress (_ActuatorID int)
BEGIN
    SELECT Address FROM Actuators WHERE ActID = _ActuatorID;
END;