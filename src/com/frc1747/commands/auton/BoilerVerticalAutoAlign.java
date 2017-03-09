package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BoilerVerticalAutoAlign extends PIDCommand {
	
	private DriveSubsystem drive;
	private double offset;
	private static final double MAX_DRIVE_SPEED = 1.5; //the max speed the robot should be driving at
	static final double P = 2;
	static final double I = 5;
	static final double D = 0;

    public BoilerVerticalAutoAlign() {   	
    	super(P,I,D);
    	super.requires(drive = DriveSubsystem.getInstance());
    }

    protected void initialize() {
    	//drive.enableSpeedPID();
    	super.setSetpoint(0);
    	SmartDashboard.putString("Boiler Aligned (Robot Side)", "RUNNING");
    }

    protected void execute() {
    	//offset = SmartDashboard.getNumber("Boiler Horizontal", 0);
    	//drive.setSetpoint(offset * MAX_DRIVE_SPEED, -offset * MAX_DRIVE_SPEED);
    	//offset just scales speed, the thresholds are set in 2017 Vision in BoilerFilterConfig
    }

    protected boolean isFinished() {
        return offset == 0;
    }

    protected void end() {
    	//drive.setSetpoint(0.0, 0.0);
    	//drive.disablePID();
    	//SmartDashboard.putString("Boiler Aligned (Robot Side)", "Aligned");
    }

    protected void interrupted() {
    	end();
    }

	@Override
	protected double returnPIDInput() {
		offset = SmartDashboard.getNumber("Boiler Vertical", 0);
		return offset;
	}

	@Override
	protected void usePIDOutput(double output) {
		//drive.setSetpoint(MAX_DRIVE_SPEED * output, MAX_DRIVE_SPEED * output);
	}
}
