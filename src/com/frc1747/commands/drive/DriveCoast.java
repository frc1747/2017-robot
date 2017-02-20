package com.frc1747.commands.drive;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;


/**
 *
 */
public class DriveCoast extends Command {
	
	private DriveSubsystem drive;
	private static final int motorDelay = 0;
	long startTime;
	long currentTime;

    public DriveCoast() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	drive = DriveSubsystem.getInstance();
    	requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = System.currentTimeMillis();
    	currentTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//drive.setPower(0.0, 0.0);
    	drive.disablePID();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	currentTime = System.currentTimeMillis();
        return currentTime - startTime >= motorDelay;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
