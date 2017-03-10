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
	public double shiftUpVel = 5.5;
	public double shiftDownVel = 3.5;
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
		if(System.currentTimeMillis() - sinceLastMeasure > 50){
			rightAccel = (driveSubsystem.getRightSpeed() - lastRightVel) / 0.05;
			leftAccel = (driveSubsystem.getLeftSpeed() - lastLeftVel) / 0.05;
			lastLeftVel = driveSubsystem.getLeftSpeed();
			lastRightVel = driveSubsystem.getRightSpeed();
			sinceLastMeasure = System.currentTimeMillis();
			avgAccel = (leftAccel + rightAccel) / 2;
//			System.out.println(avgAccel);
		}
    	//System.out.println("EXECUTE: " + shifter.isHighGear() + ", " + driveSubsystem.getLeftSpeed());
		if (shifter.isHighGear() && Math.abs(driveSubsystem.getAverageSpeed()) < shiftDownVel/*Math.abs(shiftDownVel - shiftDownSlope * avgAccel)*/){
			System.out.println("Shifting down");
			shifter.setTransmission(shifter.LOW_GEAR);
			Scheduler.getInstance().add(new DriveCoast());
		} else if (shifter.isLowGear() && Math.abs(driveSubsystem.getAverageSpeed()) > shiftUpVel/*Math.abs(shiftUpVel - shiftUpSlope*avgAccel)*/){
			System.out.println("Shifting up");
			shifter.setTransmission(shifter.HIGH_GEAR);
			Scheduler.getInstance().add(new DriveCoast());
		} else {
			shifter.setTransmission(shifter.getGear());
		}
		
		System.out.println("Average Speed " + driveSubsystem.getAverageSpeed());
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
