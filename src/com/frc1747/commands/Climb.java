package com.frc1747.commands;

import com.frc1747.Robot;
import com.frc1747.subsystems.ClimbSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {

	private ClimbSubsystem climber;
	
	public Climb() {
		
		climber = Robot.getClimber();
		requires(climber);
		
	}
	
	// Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	climber.setMotorPower(0.5);
    	// Comment
    
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    	climber.setMotorPower(0.0);
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

}
