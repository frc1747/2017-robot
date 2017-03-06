package com.frc1747.commands;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BoilerHorizontal extends Command {

	DriveSubsystem drive;
	double offset;
	double startTime;
	//static final double DEADBAND = .5;
	
    public BoilerHorizontal() {
       drive = DriveSubsystem.getInstance();
       requires(drive);
       SmartDashboard.putNumber("Gyro Setpoint", 0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	offset = SmartDashboard.getNumber("Boiler Horizontal",0);
    	drive.resetGyro();
    	drive.setGyroOffset(offset);
    	drive.setGyroSetpoint(offset);
    	drive.enableGyroPID();
    	startTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		SmartDashboard.putNumber("Gyro Angle", drive.getAngle());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drive.isGyroAtTarget() || (System.currentTimeMillis() - startTime > 5000);
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
    }
}
