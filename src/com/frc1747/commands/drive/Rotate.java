package com.frc1747.commands.drive;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Rotate extends Command {

	DriveSubsystem drive;
	double setpoint, startTime;
	
    public Rotate(double setpoint) {
    	drive = DriveSubsystem.getInstance();
    	requires(drive);
    	this.setpoint = setpoint;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.resetGyro();
    	drive.setGyroSetpoint(setpoint);
    	drive.enableGyroPID();
    	startTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (System.currentTimeMillis() - startTime > 1500);
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.disableGyroPID();
    	drive.setGyroSetpoint(0);
    	drive.resetGyro();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
