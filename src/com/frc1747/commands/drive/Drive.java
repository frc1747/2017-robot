package com.frc1747.commands.drive;

import com.frc1747.OI;
import com.frc1747.Robot;
import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import lib.frc1747.controller.Logitech;

/**
 *
 */
public class Drive extends Command {

	DriveSubsystem drivetrain;
	OI oi;
	int multiplier;
	double leftVert;
	double rightHoriz;
	
    public Drive() {
    	drivetrain = DriveSubsystem.getInstance();
    	oi = OI.getInstance();
    	requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drivetrain.enablePID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	rightHoriz = OI.getInstance().getDriver().getAxis(Logitech.RIGHT_HORIZONTAL);
    	leftVert = OI.getInstance().getDriver().getAxis(Logitech.LEFT_VERTICAL);
    	
    	//***PUT A REAL MULTIPLIER***
    	if(oi.getDriver().getButton(Logitech.LT).get()){
    		drivetrain.setSetpoint(leftVert + rightHoriz, leftVert - rightHoriz);
    	} else {
    		drivetrain.setPower(leftVert + rightHoriz, leftVert - rightHoriz);
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
