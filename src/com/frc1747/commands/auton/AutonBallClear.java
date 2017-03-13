package com.frc1747.commands.auton;

import com.frc1747.subsystems.CollectorSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonBallClear extends Command {
	
	private CollectorSubsystem intake;
	private long startTime;

    public AutonBallClear() {
        requires(intake = CollectorSubsystem.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	intake.setPower(0.85);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return startTime >= 1000;
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.setPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
