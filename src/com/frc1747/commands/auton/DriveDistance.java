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
	double leftStartPos;
	double rightStartPos;
	
    public DriveDistance(double leftPos, double rightPos) {
        requires(drive = DriveSubsystem.getInstance());
        this.leftPos = leftPos;
        this.rightPos = rightPos;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.enablePositionPID();
    	leftStartPos = drive.getLeftPosition();
    	rightStartPos = drive.getRightPosition();
    	drive.setSetpoint(leftPos, rightPos);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(leftPos + leftStartPos) == Math.abs(drive.getLeftPosition()) && Math.abs(rightPos + rightStartPos) == Math.abs(drive.getRightPosition());
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.enableSpeedPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
