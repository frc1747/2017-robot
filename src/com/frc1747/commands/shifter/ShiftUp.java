package com.frc1747.commands.shifter;

import com.frc1747.subsystems.ShifterSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShiftUp extends Command {

	private ShifterSubsystem shifter;
	
    public ShiftUp() {
    	System.out.println("ShiftUp Constructor");
    	shifter = ShifterSubsystem.getInstance();
        requires(shifter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shifter.setTransmission(shifter.HIGH_GEAR);
    	System.out.println("Shifting");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {    
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
