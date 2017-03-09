package com.frc1747.commands;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BoilerVertical extends Command {

	DriveSubsystem drive;
	double offset;
	
    public BoilerVertical() {
    	drive = DriveSubsystem.getInstance();
    	requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	offset = SmartDashboard.getNumber("Boiler Vertical", 0); //Check to make sure that positive "Boiler Vertical" means back up
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
