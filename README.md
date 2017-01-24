# 2017-robot

This is the robot project for Harrison Boiler Robotics for the 2017 FRC Season: FIRST STEAMWORKS.

## About

This robot project utilizes the Command-Based structure insituted by FIRST Robotics. Source files are seperated into three main parts. 
- 1. Commands
- 2. Subsystems
- 3. Miscellaneous

### Commands

Commands are classes that are used to run a certain set of actions. Commands are all included in the package below:

    com.frc1747.commands
    

### Subsystems

Subsystems consit of methods that can be used in Commands to make things move. Subsystems are all included in the package below:

    com.frc1747.subsystems

### Miscellaneous

There are a lot of miscellaneous files in the robot project. Current misc files include
- Robot.java
  - The backbone of the robot code. Includes methods that are used to call Auton, Teleop modes.
- OI.java
  - Shortform for "Operator Interface." Contains all button mappings that are used for joysticks and controllers.
