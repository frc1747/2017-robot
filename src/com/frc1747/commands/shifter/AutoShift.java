package com.frc1747.commands.shifter;

import com.frc1747.commands.drive.DriveCoast;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.ShifterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 *
 */
public class AutoShift extends Command {
	
	private static final double LOWER_THRESHOLD = 4.8;
	private static final double UPPER_THRESHOLD = 6.8;
	private ShifterSubsystem shifter;
	private DriveSubsystem driveSubsystem;

    public AutoShift() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	shifter = ShifterSubsystem.getInstance();
    	requires(shifter);
    	driveSubsystem = DriveSubsystem.getInstance();
    	setInterruptible(true);
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	System.out.println("EXECUTE: " + shifter.isHighGear() + ", " + driveSubsystem.getLeftSpeed());
		if (shifter.isHighGear() && Math.abs(driveSubsystem.getLeftSpeed()) < LOWER_THRESHOLD) {
			shifter.setTransmission(shifter.LOW_GEAR);
			Scheduler.getInstance().add(new DriveCoast());
		} else if (shifter.isLowGear() && Math.abs(driveSubsystem.getLeftSpeed()) > UPPER_THRESHOLD) {
			shifter.setTransmission(shifter.HIGH_GEAR);
			Scheduler.getInstance().add(new DriveCoast());
		} else if (shifter.isHighGear()) {
			shifter.setTransmission(shifter.HIGH_GEAR);
		} else {
			shifter.setTransmission(shifter.LOW_GEAR);
		}
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
