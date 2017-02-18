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
	private int counter = 0;
	private long gateTime;
	private long startTime;
	private long endTime;
	
    public Shoot() {
    	shooter = ShooterSubsystem.getInstance();
    	conveyor = ConveyorSubsystem.getInstance();
    	shooterGate = ShooterGateSubsystem.getInstance();
    	counter = 0;
    	gateTime = 170;
    	endTime = 115;
    	requires(conveyor);
    	requires(shooter);
    	requires(shooterGate);
    	
    	//good setpoint is back: 80 front: 35
    	SmartDashboard.putNumber("Front Shooter Setpoint", 30);
    	SmartDashboard.putNumber("Back Shooter Setpoint", 80);
    	SmartDashboard.putNumber("Shooter Gate Time", gateTime);
    	SmartDashboard.putNumber("Gate Open Time", endTime);
    	
    }

    protected void initialize() {
    	shooter.enablePID();
    	conveyor.enablePID();
    	conveyor.setSetpoint(400.0);
    	startTime = System.currentTimeMillis();
      	shooter.setSetpoint(SmartDashboard.getNumber("Back Shooter Setpoint", 80),
    			-SmartDashboard.getNumber("Front Shooter Setpoint", 35));
      	//conveyor.setMotorPower(conveyor.CONVEYOR_POWER);
    }

    protected void execute() {
    	//shooter.setBackPower(0.5);
    	//shooter.setFrontPower(0.5);
    	if (System.currentTimeMillis() - startTime >= SmartDashboard.getNumber("Shooter Gate Time", gateTime)) {
	    		
    		//shooterGate.setAllSolenoids(ShooterGateSubsystem.GATE_CLOSE);
    		shooterGate.gatesClose();
    		
	    	if(shooter.onTarget()) {
	    		if (counter % 2 == 0) {
	    			if(shooter.onTarget()){
	    				shooterGate.setSolenoid(1, ShooterGateSubsystem.GATE_CLOSE);
	    			}
	    		}
	    		else {
	    			if(shooter.onTarget()){
	    				shooterGate.setSolenoid(2, ShooterGateSubsystem.GATE_OPEN);
	    			}
	    		}
	    		
	    		//conveyor.setMotorPower(conveyor.CONVEYOR_POWER);
	    		startTime = System.currentTimeMillis();
	    		counter++;
	    		
	    	}
	    	else {
//	    		conveyor.setMotorPower(0.0);
	    	}
    	}
    	
    	if(System.currentTimeMillis() - startTime >= SmartDashboard.getNumber("Gate Open Time", endTime)){
    		shooterGate.gatesClose();
    	}
    	
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	//shooterGate.setAllSolenoids(ShooterGateSubsystem.GATE_CLOSE);
    	shooterGate.gatesClose();
    	shooter.disablePID();
    	shooter.setBackPower(0.0);
    	shooter.setFrontPower(0.0);
       	conveyor.disablePID();
    	conveyor.setMotorPower(0.0);
    }

    protected void interrupted() {
    	end();
    }
}
