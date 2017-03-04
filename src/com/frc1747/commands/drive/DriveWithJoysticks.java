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
public class DriveWithJoysticks extends Command {

	private DriveSubsystem drive;
	//private OI oi;
	private ShifterSubsystem shifter;
	
	int multiplier;
	double leftVert;
	double rightHoriz;
	
    public DriveWithJoysticks() {
    	requires(drive = DriveSubsystem.getInstance());
    	shifter = ShifterSubsystem.getInstance();
    	setInterruptible(true);
    	SmartDashboard.putNumber("right setpoint", 7);
    	SmartDashboard.putNumber("left setpoint", 7);
    }

    protected void initialize() {
    }
    
    protected void execute() {

//    	drive.enableSpeedPID();
    	
    	rightHoriz = OI.getInstance().getDriver().getAxis(Logitech.RIGHT_HORIZONTAL);
    	leftVert = OI.getInstance().getDriver().getAxis(Logitech.LEFT_VERTICAL);
    	drive.driveArcadeMode(leftVert, rightHoriz);
    	//if(oi.getDriver().getButton(Logitech.LT).get()){
    		
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
    		}
    			//drive.setPower(leftVert + rightHoriz, leftVert - rightHoriz);
//			drive.driveArcadePID(12.0 * Math.pow(leftVert, 3), 12.0 * Math.pow(rightHoriz, 3));
    		if(Math.abs(drive.getLeftSetpoint()) < 0.5 && Math.abs(drive.getRightSetpoint()) < 0.05) {
    			drive.disablePID();
    			drive.setPower(0, 0);
    		}
    	//drive.setSetpoint(SmartDashboard.getNumber("left setpoint", 7), SmartDashboard.getNumber("right setpoint", 7));
    }

    protected boolean isFinished() {
        return false;
    }
    
    protected void end() {
    	drive.setPower(0.0, 0.0);
    	drive.setSetpoint(0, 0);
    	drive.disablePID();
    }

    protected void interrupted() {
    	//end();
    }
}