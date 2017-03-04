package com.frc1747.commands.shooter;

import com.frc1747.subsystems.ShooterGateSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenGates extends Command {
	
	private ShooterGateSubsystem shooterGate;

    public OpenGates() {
    	
    	shooterGate = ShooterGateSubsystem.getInstance();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//    	shooterGate.gatesOpen();
    	shooterGate.setSolenoid(2, ShooterGateSubsystem.GATE_OPEN);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
//    	shooterGate.setSolenoid(1, ShooterGateSubsystem.GATE_HOLD);
    	shooterGate.setSolenoid(2, ShooterGateSubsystem.GATE_CLOSE);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
