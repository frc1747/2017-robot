package com.frc1747.commands.drive;

import com.frc1747.OI;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.ShifterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.controller.Logitech;

/**
 * Default command to control with the robot drive using Joysticks
 * Runs as long as there is not a another drive command overriding it.
 */
public class DriveWithJoysticks_OLD extends Command {

	private DriveSubsystem drive;
	//private OI oi;
	private ShifterSubsystem shifter;
	
	int multiplier;
	double leftVert;
	double rightHoriz;
	
    public DriveWithJoysticks_OLD() {
    	requires(drive = DriveSubsystem.getInstance());
    	shifter = ShifterSubsystem.getInstance();
    	setInterruptible(true);
    }

    protected void initialize() {
    }
    
    protected void execute() {
    	
    	rightHoriz = OI.getInstance().getDriver().getAxis(Logitech.RIGHT_HORIZONTAL);
    	leftVert = OI.getInstance().getDriver().getAxis(Logitech.LEFT_VERTICAL);

		drive.driveArcadeMode(Math.pow(leftVert, 3), Math.pow(rightHoriz, 3));
    	/*
    	if(shifter.isHighGear()){
			if(drive.getLeftSetpoint() < 0){
				drive.setLeftPIDF(DriveSubsystem.leftHighPIDBackward);
			}else{
				drive.setLeftPIDF(DriveSubsystem.leftHighPIDForward);
			}
			if(drive.getRightSetpoint() < 0){
				drive.setRightPIDF(DriveSubsystem.rightHighPIDBackward);
			}else{
				drive.setRightPIDF(DriveSubsystem.rightHighPIDForward);
			}
		}
		else {
			if(drive.getLeftSetpoint() < 0){
				drive.setLeftPIDF(DriveSubsystem.leftLowPIDBackward);
			}else{
				drive.setLeftPIDF(DriveSubsystem.leftLowPIDForward);
			}
			if(drive.getRightSetpoint() < 0){
				drive.setRightPIDF(DriveSubsystem.rightLowPIDBackward);
			}else{
				drive.setRightPIDF(DriveSubsystem.rightLowPIDForward);
			}
		}*/
    }

    protected boolean isFinished() {
        return false;
    }
    
    protected void end() {
    	drive.setPower(0.0, 0.0);
    }

    protected void interrupted() {
    	//end();
    }
}