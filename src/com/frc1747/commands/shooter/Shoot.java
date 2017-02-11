package com.frc1747.commands.shooter;

import com.frc1747.subsystems.ConveyorSubsystem;
import com.frc1747.subsystems.ShooterGateSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shoot extends Command {

	private ShooterSubsystem shooter;
	private ConveyorSubsystem conveyor;
	private ShooterGateSubsystem shooterGate;
	private int counter;
	private long gateTime;
	private long startTime;
	private long endTime;
	
    public Shoot() {
    	shooter = ShooterSubsystem.getInstance();
    	conveyor = ConveyorSubsystem.getInstance();
    	shooterGate = ShooterGateSubsystem.getInstance();
    	counter = 0;
    	gateTime = 500;
    	requires(conveyor);
    	requires(shooter);
    	requires(shooterGate);
    	SmartDashboard.putNumber("Front Shooter Setpoint", 55);
    	SmartDashboard.putNumber("Back Shooter Setpoint", 70);
    }

    protected void initialize() {
    	shooter.enablePID();
    	startTime = System.currentTimeMillis();
    }

    protected void execute() {
    	shooter.setSetpoint(SmartDashboard.getNumber("Back Shooter Setpoint", 70),
    			-SmartDashboard.getNumber("Front Shooter Setpoint", 55));
    	if (System.currentTimeMillis() - startTime >= gateTime) {
	    		
    		shooterGate.setSolenoid1(ShooterGateSubsystem.GATE_CLOSE);
    		shooterGate.setSolenoid2(ShooterGateSubsystem.GATE_CLOSE);
    		shooterGate.setSolenoid3(ShooterGateSubsystem.GATE_CLOSE);
    		
	    	if(shooter.isAtTarget()) {
	    		if (counter % 3 == 0) {
	    			shooterGate.setSolenoid1(ShooterGateSubsystem.GATE_OPEN);
	    		}
	    		else if (counter % 3 == 1) {
	    			shooterGate.setSolenoid2(ShooterGateSubsystem.GATE_OPEN);
	    		}
	    		else {
	    			shooterGate.setSolenoid3(ShooterGateSubsystem.GATE_OPEN);
	    		}
	    		
	    		conveyor.setMotorPower(conveyor.CONVEYOR_POWER);
	    		startTime = System.currentTimeMillis();
	    		counter += 1;
	    		
	    	}
	    	else {
	    		conveyor.setMotorPower(0.0);
	    	}
	    	//conveyor.setSetpoint(0.0);
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	shooterGate.setSolenoid1(ShooterGateSubsystem.GATE_CLOSE);
		shooterGate.setSolenoid2(ShooterGateSubsystem.GATE_CLOSE);
		shooterGate.setSolenoid3(ShooterGateSubsystem.GATE_CLOSE);
    	shooter.disablePID();
    	shooter.setBackPower(0.0);
    	shooter.setFrontPower(0.0);
       	conveyor.setMotorPower(0.0);
    }

    protected void interrupted() {
    	end();
    }
}
