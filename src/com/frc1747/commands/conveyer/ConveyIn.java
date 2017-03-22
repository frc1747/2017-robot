package com.frc1747.commands.conveyer;

import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ConveyIn extends Command{

	private ConveyorSubsystem conveyor;
	private CollectorSubsystem intake;
	
	public ConveyIn() {
		conveyor = ConveyorSubsystem.getInstance();
		//conveyor.setPIDF(kP, kI, kD, kF);
		//conveyor.enablePID();
		requires(conveyor);
		intake = CollectorSubsystem.getInstance();
	}
	
	
	 // Called just before this Command runs the first time
    protected void initialize() {
    	conveyor.enablePID();
    	//System.out.println("INIT");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//System.out.println("EXEC");
    	//conveyor.setMotorPower(conveyor.CONVEYOR_POWER);
    	if(intake.isIntakeOut()){
    		conveyor.setSetpoint(200.0);
    	}else{
    		conveyor.setSetpoint(0.0);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	conveyor.disablePID();
    	conveyor.setMotorPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

}
