package com.frc1747.commands;

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
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	

    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if(SmartDashboard.getString("Targeted", "Do nothing") == "Targeted"){
        	drive.setSetpoint(0.0, 0.0);
    	}
    	else if(SmartDashboard.getString("Targeted", "Do nothing") == "Turn Left"){
        	drive.setSetpoint(0.3, -0.3);
        }
        else if(SmartDashboard.getString("Targeted", "Do nothing") == "Turn Right"){
        	drive.setSetpoint(-0.3, 0.3);
        }
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.setSetpoint(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
