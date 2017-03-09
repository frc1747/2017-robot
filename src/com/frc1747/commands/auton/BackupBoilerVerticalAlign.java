package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BackupBoilerVerticalAlign extends Command {
	
	DriveSubsystem drive;
	double offset;
	static final double MAX_SPEED = 1.5;
	
    public BackupBoilerVerticalAlign() {
       drive = DriveSubsystem.getInstance();
       requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//System.out.println("Running");
    	//drive.enableSpeedPID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	offset = SmartDashboard.getNumber("Boiler Vertical", 1);
    	System.out.print(offset);
    	//drive.setSetpoint(-MAX_SPEED*offset, -MAX_SPEED*offset);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return offset == 0; 
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("Aligned");
    	//drive.setSetpoint(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//drive.setSetpoint(0,0);
    }
}
