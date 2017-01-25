package com.frc1747.commands;

import com.frc1747.Robot;
import com.frc1747.subsystems.Conveyor;

import edu.wpi.first.wpilibj.command.Command;

public class ConveyIn extends Command{

	private Conveyor conveyor;
	
	public ConveyIn() {
		
		conveyor = Robot.getConveyor();
		requires(conveyor);
		
	}
	
	
	 // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	conveyor.setMotorPower(.5);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	conveyor.setMotorPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

}
