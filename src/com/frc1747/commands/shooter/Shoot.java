package com.frc1747.commands.shooter;

import com.frc1747.subsystems.ConveyorSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shoot extends Command {

	private ShooterSubsystem shooter;
	private ConveyorSubsystem conveyor;
	
    public Shoot() {
    	shooter = ShooterSubsystem.getInstance();
    	conveyor = ConveyorSubsystem.getInstance();
    	requires(conveyor);
    	requires(shooter);
    	SmartDashboard.putNumber("Front Shooter Setpoint", 55);
    	SmartDashboard.putNumber("Back Shooter Setpoint", 70);
    }

    protected void initialize() {
    	shooter.enablePID();
    }

    protected void execute() {
    	shooter.setSetpoint(SmartDashboard.getNumber("Back Shooter Setpoint", 70),
    			-SmartDashboard.getNumber("Front Shooter Setpoint", 55));
    	if(shooter.isAtTarget()) {
    		conveyor.setMotorPower(conveyor.CONVEYOR_POWER);
    	}
    	else {
    		conveyor.setMotorPower(0.0);
    	}
    	//conveyor.setSetpoint(0.0);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	shooter.disablePID();
    	shooter.setBackPower(0.0);
    	shooter.setFrontPower(0.0);
       	conveyor.setMotorPower(0.0);
    }

    protected void interrupted() {
    	end();
    }
}
