package com.frc1747.commands.collector;

import com.frc1747.subsystems.CollectorSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Retract extends Command {
	
	private CollectorSubsystem collector;
	
    public Retract() {
    	collector = CollectorSubsystem.getInstance();
    	requires(collector);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	collector.setPosition(collector.RETRACT_POSITION);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
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
