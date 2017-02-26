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
	
	//private static final double LOWER_THRESHOLD = 4.8;
	//private static final double UPPER_THRESHOLD = 6.8;
	public double shiftUpSlope = 7./25;
	public double shiftDownSlope = 5./15;
	public double shiftUpVel = 6.5;
	public double shiftDownVel = 4.5;
	public double avgAccel;
	private ShifterSubsystem shifter;
	private DriveSubsystem driveSubsystem;
	
	//private long lastMeasure;
	private long sinceLastMeasure;
	private double lastLeftVel;
	private double lastRightVel;
	public double rightAccel;
	public double leftAccel;

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
    	//lastMeasure = System.currentTimeMillis();
    	lastLeftVel = 0;
    	lastRightVel = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {		
		if(sinceLastMeasure - System.currentTimeMillis() > 50){
			rightAccel = (driveSubsystem.getRightFeetPerSecond() - lastRightVel) / 0.05;
			leftAccel = (driveSubsystem.getLeftFeetPerSecond() - lastLeftVel) / 0.05;
			lastLeftVel = driveSubsystem.getLeftFeetPerSecond();
			lastRightVel = driveSubsystem.getRightFeetPerSecond();
			//lastMeasure = System.currentTimeMillis();
			avgAccel = (leftAccel + rightAccel) / 2;
		}
    	//System.out.println("EXECUTE: " + shifter.isHighGear() + ", " + driveSubsystem.getLeftSpeed());
		if (shifter.isHighGear() && driveSubsystem.getAverageSpeed() < shiftDownVel - shiftDownSlope * avgAccel){
			shifter.setTransmission(shifter.LOW_GEAR);
			Scheduler.getInstance().add(new DriveCoast());
		} else if (shifter.isLowGear() && driveSubsystem.getAverageSpeed() > shiftUpVel - shiftUpSlope*avgAccel){
			shifter.setTransmission(shifter.HIGH_GEAR);
			Scheduler.getInstance().add(new DriveCoast());
		} else {
			shifter.setTransmission(shifter.getGear());
		}
			/*if (shifter.isHighGear()) {
			shifter.setTransmission(shifter.HIGH_GEAR);
		} else {
			shifter.setTransmission(shifter.LOW_GEAR);
		}*/
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
