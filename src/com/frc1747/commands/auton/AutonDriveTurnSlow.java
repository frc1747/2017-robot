package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class AutonDriveTurnSlow extends Command {
	
	private DriveSubsystem drive;
	double angle;
	double distance;
	
    public AutonDriveTurnSlow(double distance, double angle) {
    	requires(drive = DriveSubsystem.getInstance());
    	drive = DriveSubsystem.getInstance();
    	this.angle = (angle / 360) * 2 * Math.PI;
    	this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double[][][] profiles = HBRSubsystem.generateSkidSteerPseudoProfile(distance, angle, 2);

    	drive.setMode(DriveSubsystem.Follower.DISTANCE, HBRSubsystem.Mode.FOLLOWER);
    	drive.setPIDMode(DriveSubsystem.Follower.DISTANCE, HBRSubsystem.PIDMode.POSITION);
    	drive.setILimit(DriveSubsystem.Follower.DISTANCE, 0);
    	drive.setFeedforward(DriveSubsystem.Follower.DISTANCE, 0, 0.165, 0.01625);
    	drive.setFeedback(DriveSubsystem.Follower.DISTANCE, 0.5750 / 2/*0.75*/, 0.00745/*0.015*/, 0);
    	drive.resetIntegrator(DriveSubsystem.Follower.DISTANCE);
    	drive.setProfile(DriveSubsystem.Follower.DISTANCE, profiles[0]);
    	
    	// Setup right side
    	drive.setMode(DriveSubsystem.Follower.ANGLE, HBRSubsystem.Mode.FOLLOWER);
    	drive.setPIDMode(DriveSubsystem.Follower.ANGLE, HBRSubsystem.PIDMode.POSITION);
    	drive.setILimit(DriveSubsystem.Follower.ANGLE, 0);
    	drive.setFeedforward(DriveSubsystem.Follower.ANGLE, 0, 0.18, 0.03);
    	drive.setFeedback(DriveSubsystem.Follower.ANGLE, 10, 0.2, 0);
    	drive.resetIntegrator(DriveSubsystem.Follower.ANGLE);
    	drive.setProfile(DriveSubsystem.Follower.ANGLE, profiles[1]);
    	    	
    	// Enable the pids
    	drive.resume(DriveSubsystem.Follower.DISTANCE);
    	drive.resume(DriveSubsystem.Follower.ANGLE);
    	drive.setEnabled(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//System.out.println("PROFILE FINISHED?");
        return !drive.isRunning(DriveSubsystem.Follower.DISTANCE) && !drive.isRunning(DriveSubsystem.Follower.ANGLE);
    }

    // Called when the command ends
    protected void end() {
    	drive.setEnabled(false);
    	drive.setPower(0, 0);
    }

    // Called when the command is interrupted
    protected void interrupted() {
    	end();
    }
}
