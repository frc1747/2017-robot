package com.frc1747.commands;

import com.frc1747.Robot;
import com.frc1747.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TakeIn extends Command {

	Intake intake;
	double power;
	
    public TakeIn() {
    	intake = Robot.getIntake();
    	requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	//***PUT A REAL VALUE FOR POWER***
    	intake.in(power);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
