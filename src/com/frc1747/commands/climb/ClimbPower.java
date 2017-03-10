package com.frc1747.commands.climb;

import com.frc1747.subsystems.ClimbSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbPower extends Command {

	private ClimbSubsystem climber;
	private double power;
    public ClimbPower(double power) {
        requires(climber = ClimbSubsystem.getInstance());
        this.power = power;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	climber.setMotorPower(power);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	climber.setMotorPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
