package com.frc1747.commands.conveyer;

import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ConveyOut extends Command {
	
	private ConveyorSubsystem conveyor;
	private CollectorSubsystem intake;

    public ConveyOut() {
    	
    	conveyor = ConveyorSubsystem.getInstance();
    	requires(conveyor);
    	requires(intake = CollectorSubsystem.getInstance());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(intake.isIntakeOut()){
    		conveyor.setMotorPower(-conveyor.CONVEYOR_POWER);
    	}else{
    		conveyor.setMotorPower(0.0);
    	}
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
    	end();
    }
}
