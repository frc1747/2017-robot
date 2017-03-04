package com.frc1747.commands.climb;

import com.frc1747.subsystems.ClimbSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class StopClimb extends Command {

	private ClimbSubsystem climber;
	
	public StopClimb() {
		climber = ClimbSubsystem.getInstance();
		requires(climber);
	}
	
	// Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	climber.setMotorPower(0.0);    
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {    	
    	climber.setMotorPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

}
