package com.frc1747.commands;

import com.frc1747.subsystems.ClimbSubsystem;
import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.ShifterSubsystem;
import com.frc1747.subsystems.ShooterGateSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    	
    	if(timer.get() > 1.0 / 20){
	    	ClimbSubsystem.getInstance().updateDashboard();
			CollectorSubsystem.getInstance().updateDashboard();
			ConveyorSubsystem.getInstance().updateDashboard();
			DriveSubsystem.getInstance().updateDashboard();
			ShooterSubsystem.getInstance().updateDashboard();
			ShooterGateSubsystem.getInstance().updateDashboard();
			ShifterSubsystem.getInstance().updateDashboard();
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