package com.frc1747.commands.gear;

import com.frc1747.subsystems.GearSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearMechClose extends Command {
	
	private GearSubsystem gear;
	
    public GearMechClose() {
    	requires(gear = GearSubsystem.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	gear.closeGearMech();
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
