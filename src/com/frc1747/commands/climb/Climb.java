package com.frc1747.commands.climb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.frc1747.OI;
import com.frc1747.subsystems.ClimbSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import lib.frc1747.controller.Logitech;
import lib.frc1747.controller.Xbox;

public class Climb extends Command {

	private ClimbSubsystem climber;
	private PrintWriter print;
	
	public Climb() {
		////System.out.println("Climb constructor");
		climber = ClimbSubsystem.getInstance();
		requires(climber);
		
		// Initialize logging
		try{
			print = new PrintWriter(
					new FileOutputStream(
					File.createTempFile("log_climb_", ".csv",
							new File("/home/lvuser")), true));
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//climber.setMotorPower(Math.abs(OI.getInstance().getOperator().getAxis(Logitech.LEFT_VERTICAL)));
    	climber.setMotorPower(OI.getInstance().getOperator().getLTAngle());
    	
    	// Logging
		if(print != null) {
			print.format("%.4f, %.4f, %.4f\n", climber.getCurrent(), climber.getVoltage(), climber.getBusVoltage());
			//System.out.println("LOG");
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {    	
    	climber.setMotorPower(0.0);
    	if(print != null) {
			print.flush();
			print.close();
		}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

}
