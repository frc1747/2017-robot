package com.frc1747.commands.conveyer;

import com.frc1747.subsystems.ConveyorSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ConveyOut extends Command {
	
	private ConveyorSubsystem conveyor;

    public ConveyOut() {
    	
    	conveyor = ConveyorSubsystem.getInstance();
    	requires(conveyor);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	conveyor.setMotorPower(-conveyor.CONVEYOR_POWER);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	conveyor.setMotorPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
