package com.frc1747.commands;

import com.frc1747.OI;
import com.frc1747.Robot;
import com.frc1747.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Drive extends Command {

	Drivetrain drivetrain;
	OI oi;
	int multiplier;
	
    public Drive() {
    	drivetrain = Robot.getDrivetrain();
    	oi = Robot.getOI();
    	requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drivetrain.enablePID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	//***PUT A REAL MULTIPLIER***
    	if(oi.getController().getButtonLT().get()){
    		drivetrain.setSetpoint(multiplier*oi.getController().getLeftVertical() + multiplier*oi.getController().getRightHorizontal(), multiplier*oi.getController().getLeftVertical() - multiplier*oi.getController().getRightHorizontal());
    	} else {
    		drivetrain.setPower(oi.getController().getLeftVertical() + oi.getController().getRightHorizontal(), oi.getController().getLeftVertical() - oi.getController().getRightHorizontal());
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drivetrain.setSetpoint(0, 0);
    	//drivetrain.disablePID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
