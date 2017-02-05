package com.frc1747.commands;

import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MotorTest extends Command {

	private ShooterSubsystem shooter;
	
    public MotorTest() {
    	
    	shooter = ShooterSubsystem.getInstance();
    	requires(shooter);
    	SmartDashboard.putNumber("SetpointFront", 65);
    	SmartDashboard.putNumber("setpointBack", 65);
    	
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooter.enablePID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	shooter.setSetpoint(SmartDashboard.getNumber("setpointBack", 65), -SmartDashboard.getNumber("SetpointFront", 65));

    	//shooterSubsystem.setSetpoint(0, -SmartDashboard.getNumber("Setpoint", 75));
    	//shooterSubsystem.setTopPower(-0.85);
    	//shooterSubsystem.setBottomPower(-0.85);
    	
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
    	end();
    }
}
