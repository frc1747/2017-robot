package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RotateAngle extends Command {

	DriveSubsystem drive;
	double angle;
	double circleRadius;
	double turnDist;
	
    public RotateAngle(double angle) {
        requires(drive = DriveSubsystem.getInstance());
        this.angle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.enablePositionPID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	turnDist = (angle / 360) * Math.PI * 2 * circleRadius; //angle measured clockwise from positive y
    	drive.setSetpoint(turnDist, -turnDist);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
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
