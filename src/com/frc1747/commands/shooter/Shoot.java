package com.frc1747.commands.shooter;

import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shoot extends Command {

	private ShooterSubsystem shooter;
	
    public Shoot() {
    	shooter = ShooterSubsystem.getInstance();
    	requires(shooter);
    	SmartDashboard.putNumber("Front Shooter Setpoint", 65);
    	SmartDashboard.putNumber("Back Shooter Setpoint", 65);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooter.enablePID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	shooter.setSetpoint(SmartDashboard.getNumber("Back Shooter Setpoint", 65),
    			-SmartDashboard.getNumber("Fron Shooter Setpoint", 65));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooter.disablePID();
    	shooter.setBackPower(0.0);
    	shooter.setFrontPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
