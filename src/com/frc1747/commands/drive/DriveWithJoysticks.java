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
	private OI oi;
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
    	drive.enablePID();
    }
    
    protected void execute() {
    	
    	rightHoriz = OI.getInstance().getDriver().getAxis(Logitech.RIGHT_HORIZONTAL);
    	leftVert = OI.getInstance().getDriver().getAxis(Logitech.LEFT_VERTICAL);
    	
    	//if(oi.getDriver().getButton(Logitech.LT).get()){
    		if(shifter.isHighGear()){
    			drive.setSetpoint(12.0 * (leftVert + rightHoriz), 12.0 * (leftVert - rightHoriz));
    		}else{
    			//drive.setSetpoint(7.25 * (leftVert + rightHoriz), 7.25 * (leftVert - rightHoriz));
    		}
    			
    	//drive.setSetpoint(SmartDashboard.getNumber("left setpoint", 7), SmartDashboard.getNumber("right setpoint", 7));
    	if(drive.getLeftSetpoint() < 0) {
    		//drive.setLeftPIDF(DriveSubsystem.leftPIDBackward);
    	}
    	else {
    		//drive.setLeftPIDF(DriveSubsystem.leftPIDForward);
    	}
    	
    	if(drive.getRightSetpoint() < 0){
    		//drive.setRightPIDF(DriveSubsystem.rightPIDBackward);
    	}else{
    		//drive.setRightPIDF(DriveSubsystem.rightPIDForward);
    	}
    	//} else {
    		//drive.driveArcadeMode(leftVert, rightHoriz);
    	//}
    	
    	//if(shifter.shouldShiftUp()){
    		//shifter.setTransmission(shifter.HIGH_GEAR);
    	//} else{
    	//	shifter.setTransmission(shifter.LOW_GEAR);
    	//}
    }

    protected boolean isFinished() {
        return false;
    }
    
    protected void end() {
    	//drive.setPower(0.0, 0.0);
    	drive.setSetpoint(0, 0);
    	drive.disablePID();
    }

    protected void interrupted() {
    	//end();
    }
}