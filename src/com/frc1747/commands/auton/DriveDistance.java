package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDistance extends Command {
	
	DriveSubsystem drive;
	double leftPos;
	double rightPos;

    public DriveDistance(double leftPos, double rightPos) {
        requires(drive = DriveSubsystem.getInstance());
        this.leftPos = leftPos;
        this.rightPos = rightPos;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.enablePositionPID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drive.setSetpoint(leftPos, rightPos);
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
