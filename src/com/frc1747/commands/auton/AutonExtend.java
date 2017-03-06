package com.frc1747.commands.auton;

import com.frc1747.subsystems.CollectorSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonExtend extends Command {
	
	CollectorSubsystem collector;
	long startTime;

    public AutonExtend() {
        requires(collector = CollectorSubsystem.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = System.currentTimeMillis();
    	collector.setPosition(collector.EXTEND_POSITION);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() - startTime > 1000;
    }

    // Called once after isFinished returns true
    protected void end() {
    	collector.setPosition(collector.HOLD_POSITION);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
