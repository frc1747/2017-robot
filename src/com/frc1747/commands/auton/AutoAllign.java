package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoAllign extends Command {
	
	private DriveSubsystem drive;

    public AutoAllign() {   	
    	requires(drive = DriveSubsystem.getInstance());
    }

    protected void initialize() {
    	
    }

    protected void execute() {
    	
    	if(SmartDashboard.getString("Targeted", "Do nothing") == "Targeted"){
        	drive.setSetpoint(0.0, 0.0);
        	end();
    	}
    	else if(SmartDashboard.getString("Targeted", "Do nothing") == "Turn Left"){
        	drive.setSetpoint(0.3, -0.3);
        }
        else if(SmartDashboard.getString("Targeted", "Do nothing") == "Turn Right"){
        	drive.setSetpoint(-0.3, 0.3);
        }
        
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	drive.setSetpoint(0.0, 0.0);
    }

    protected void interrupted() {
    	end();
    }
}
