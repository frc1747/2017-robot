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
    	SmartDashboard.putNumber("Front Shooter Setpoint", 55);
    	SmartDashboard.putNumber("Back Shooter Setpoint", 70);
    }

    protected void initialize() {
    	shooter.enablePID();
    }

    protected void execute() {
    	shooter.setSetpoint(SmartDashboard.getNumber("Back Shooter Setpoint", 70),
    			-SmartDashboard.getNumber("Front Shooter Setpoint", 55));
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	shooter.disablePID();
    	shooter.setBackPower(0.0);
    	shooter.setFrontPower(0.0);
    }

    protected void interrupted() {
    	end();
    }
}
