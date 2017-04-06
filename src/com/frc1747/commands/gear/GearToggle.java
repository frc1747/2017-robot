package com.frc1747.commands.gear;

import com.frc1747.subsystems.GearSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearToggle extends Command {

	private GearSubsystem gear;
	
    public GearToggle() {
        requires(gear = GearSubsystem.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	gear.openGearMech();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	gear.closeGearMech();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
