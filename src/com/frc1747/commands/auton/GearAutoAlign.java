package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearAutoAlign extends Command {
	
	private DriveSubsystem drive;
	private double offset;
	private static final double MAX_DRIVE_SPEED = 1; //the max speed the robot should be turning at

    public GearAutoAlign() {   	
    	requires(drive = DriveSubsystem.getInstance());
    }

    protected void initialize() {
    	drive.enableSpeedPID();
    }

    protected void execute() {
    	offset = SmartDashboard.getNumber("Gear Targeted", 0);
    	drive.setSetpoint(offset * MAX_DRIVE_SPEED, -offset * MAX_DRIVE_SPEED);
    	//offset just scales speed, the thresholds are set in 2017 Vision in GearFilterConfig
    }

    protected boolean isFinished() {
        return offset == 0;
    }

    protected void end() {
    	drive.setSetpoint(0.0, 0.0);
    	drive.disablePID();
    }

    protected void interrupted() {
    	end();
    }
}