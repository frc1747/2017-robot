package com.frc1747.commands;

import com.frc1747.OI;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.controller.Logitech;

/**
 *
 */
public class UpdateDashboard extends Command {

    Timer timer;
	
	public UpdateDashboard() {
    	timer = new Timer();
    	setRunWhenDisabled(true);
    }

    protected void initialize() {
    	timer.reset();
    	timer.start();
    }

    protected void execute() {
    	
    	if(timer.get() > 0.1){
    		SmartDashboard.putBoolean("Up", OI.getInstance().getDriver().getDPADButton(Logitech.UP));
	    	SmartDashboard.putBoolean("Down", OI.getInstance().getDriver().getDPADButton(Logitech.DOWN));
	    	SmartDashboard.putBoolean("Left", OI.getInstance().getDriver().getDPADButton(Logitech.LEFT));
	    	SmartDashboard.putBoolean("Right", OI.getInstance().getDriver().getDPADButton(Logitech.RIGHT));
	    	SmartDashboard.putBoolean("Up Right", OI.getInstance().getDriver().getDPADButton(Logitech.UP_RIGHT));
	    	SmartDashboard.putBoolean("Up Left", OI.getInstance().getDriver().getDPADButton(Logitech.UP_LEFT));
	    	SmartDashboard.putBoolean("Down Right", OI.getInstance().getDriver().getDPADButton(Logitech.DOWN_RIGHT));
	    	SmartDashboard.putBoolean("Down Left", OI.getInstance().getDriver().getDPADButton(Logitech.DOWN_LEFT));
	    	//DriveSubsystem.getInstance().updateDashboard();
	    	ShooterSubsystem.getInstance().updateDashboard();
	    	timer.reset();
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
