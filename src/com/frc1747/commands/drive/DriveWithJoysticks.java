package com.frc1747.commands.drive;

import com.frc1747.OI;
import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import lib.frc1747.controller.Logitech;

/**
 * Default command to control with the robot drive using Joysticks
 * Runs as long as there is not a another drive command overriding it.
 */
public class DriveWithJoysticks extends Command {

	private DriveSubsystem drive;
	private OI oi;
	
	int multiplier;
	double leftVert;
	double rightHoriz;
	
    public DriveWithJoysticks() {
    	requires(drive = DriveSubsystem.getInstance());
    	oi = OI.getInstance();
    }

    protected void initialize() {
    	drive.enablePID();
    }
    
    protected void execute() {
    	
    	rightHoriz = OI.getInstance().getDriver().getAxis(Logitech.RIGHT_HORIZONTAL);
    	leftVert = OI.getInstance().getDriver().getAxis(Logitech.LEFT_VERTICAL);
    	
    	if(oi.getDriver().getButton(Logitech.LT).get()){
    		drive.setSetpoint(leftVert + rightHoriz, leftVert - rightHoriz);
    	} else {
    		drive.driveArcadeMode(leftVert, rightHoriz);
    	}
    	
    	if(drive.shouldShiftUp()){
    		drive.shiftUp();
    	} else{
    		drive.shiftDown();
    	}
    }

    protected boolean isFinished() {
        return false;
    }
    
    protected void end() {
    	drive.setSetpoint(0, 0);
    	drive.disablePID();
    }

    protected void interrupted() {
    	drive.disablePID();
    }
}
