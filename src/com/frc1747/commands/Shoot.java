package com.frc1747.commands;

import com.frc1747.Robot;
import com.frc1747.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Shoot extends Command {

	private Shooter shooter;
	double speed;
	
    public Shoot() {
        shooter = Robot.getShooter();
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooter.enablePID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	//***NOTE***: no specific speed rn
    	shooter.shootTop(speed);
    	shooter.shootBottom(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooter.shootTop(0);
    	shooter.shootBottom(0);
    	//shooter.disablePID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
