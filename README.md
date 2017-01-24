# 2017-robot

This is the robot project for Harrison Boiler Robotics for the 2017 FRC Season: FIRST STEAMWORKS.

## About this Robot

This robot contains five different main subsystems. The subsystems are as follows:
- Drive Train
  - 4 Motors (2 on each side) - CIMs
  - 2 Shifters (Pneumatics on each side)
  - 2 Encoders (One on each side)
- Intake
  - 1 Motor (Collecting in / out)
  - 1 Pneumatic (Up and Down)
- Conveyor  
  - Moves the balls from the hopper to the shooter.
  - 1 Motor (775 PRO)
- Shooter
  - 2 Motors
  - 2 Encoders on the Motors (12 cnts / rev)
- Climber
  - 2 MiniSIMs
  - Use current to measure when latched on. (Look for current spike). Spins when is launched

## Project Structure

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
