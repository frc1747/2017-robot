package com.frc1747.commands.climb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.frc1747.subsystems.ClimbSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbPower extends Command {

	private ClimbSubsystem climber;
	private double power;
	private PrintWriter print;
    public ClimbPower(double power) {
        requires(climber = ClimbSubsystem.getInstance());
        this.power = power;
        
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
    	climber.setMotorPower(power);
		if(print != null) {
			print.format("%.4f, %.4f, %.4f\n", climber.getCurrent(), climber.getVoltage(), climber.getBusVoltage());
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	climber.setMotorPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
