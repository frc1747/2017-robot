package com.frc1747.commands;

import com.frc1747.OI;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;
import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.controller.Logitech;

/**
 *
 */
public class UpdateDashboard extends Command {

    Timer timer;
    OI oi;
	
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
	    	DriveSubsystem.getInstance().updateDashboard();
	    	ShooterSubsystem.getInstance().updateDashboard();
	    	ConveyorSubsystem.getInstance().updateDashboard();
	    	CollectorSubsystem.getInstance().updateDashboard();
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
