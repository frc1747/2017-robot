package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearAutoAlign extends PIDCommand {
	
	private DriveSubsystem drive;
	private double offset;
	private static final double MAX_DRIVE_SPEED = 1; //TODO: the max speed the robot should be turning at
	private static final double P = 0;
	private static final double I = 0;
	private static final double D = 0;


    public GearAutoAlign() {
    	super(P, I, D);
    	super.requires(drive = DriveSubsystem.getInstance());
    }

    protected void initialize() {
    	drive.enableSpeedPID();
    	super.setSetpoint(0);
    }

    protected void execute() {
    	//offset = SmartDashboard.getNumber("Gear Targeted", 0);
    	//drive.setSetpoint(offset * MAX_DRIVE_SPEED, -offset * MAX_DRIVE_SPEED);
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

	@Override
	protected double returnPIDInput(){
		offset = SmartDashboard.getNumber("Gear Targeted", 0);
		return offset;
	}

	@Override
	protected void usePIDOutput(double output) {
		drive.setSetpoint(MAX_DRIVE_SPEED * output, -output * MAX_DRIVE_SPEED);
	}
}