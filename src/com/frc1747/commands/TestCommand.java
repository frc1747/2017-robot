package com.frc1747.commands;

import edu.wpi.first.wpilibj.command.Command;

/*
 * This is a test command that does not do anything. Its purpose
 * is to illustrate its function and use in a robot project. It is
 * good practice to make a comment like this at the top of your 
 * command in so that it makes it easier to underestand what the 
 * command does.
 */
public class TestCommand extends Command {

	// Use requires() in here to declare subsystem dependencies
	// e.g. requires(chassis);
    public TestCommand() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
