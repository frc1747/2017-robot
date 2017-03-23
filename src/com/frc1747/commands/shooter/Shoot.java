package com.frc1747.commands.shooter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.frc1747.Robot;
import com.frc1747.subsystems.CollectorSubsystem;
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
	private CollectorSubsystem intake;
	private int counter = 0;
	private long gateTime;
	private long startTime;
	private long endTime;
	private long pidStartTime;
	private double desiredFrontSetpoint = -35.0;
	private double desiredBackSetpoint = 120.0;
	private int rampTime;
	private PrintWriter print;
	
    public Shoot() {
    	shooter = ShooterSubsystem.getInstance();
    	conveyor = ConveyorSubsystem.getInstance();
    	shooterGate = ShooterGateSubsystem.getInstance();
    	counter = 0;
    	gateTime = 170;
    	endTime = 500;
    	requires(conveyor);
    	requires(shooter);
    	requires(shooterGate);    	
    	requires(intake = CollectorSubsystem.getInstance());
    	
    	//good setpoint is back: 80 front: 35
    	SmartDashboard.putNumber("Front Shooter Setpoint", -desiredFrontSetpoint);
    	SmartDashboard.putNumber("Back Shooter Setpoint", desiredBackSetpoint);
    	SmartDashboard.putNumber("Shooter Gate Time", gateTime);
    	SmartDashboard.putNumber("Gate Open Time", endTime);
    	SmartDashboard.putNumber("Conveyor Setpoint", 200);
    	
		// Initialize logging
		try{
			print = new PrintWriter(
					new FileOutputStream(
					File.createTempFile("log_shoot_", ".csv",
							new File("/home/lvuser")), true));
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
    	
    }

    protected void initialize() {
    	shooter.enablePID();
    	conveyor.enablePID();
    	startTime = System.currentTimeMillis();
    	pidStartTime = System.currentTimeMillis();
    	rampTime = 500;
    	desiredFrontSetpoint = -SmartDashboard.getNumber("Front Shooter Setpoint", 35);
    	desiredBackSetpoint = -SmartDashboard.getNumber("Back Shooter Setpoint", 89);
    	Robot.getCompressor().stop();
      	/*shooter.setSetpoint(SmartDashboard.getNumber("Back Shooter Setpoint", 75.5),
    			-SmartDashboard.getNumber("Front Shooter Setpoint", 35));*/
      	//conveyor.setMotorPower(conveyor.CONVEYOR_POWER);
    }

    protected void execute() {
    	//shooter.setBackPower(0.5);
    	//shooter.setFrontPower(0.5);
    	shooter.debug();
    	if(System.currentTimeMillis() - pidStartTime < rampTime){
    		shooter.setSetpoint((desiredBackSetpoint/rampTime) * (System.currentTimeMillis() - pidStartTime),
    				(desiredFrontSetpoint/rampTime) * (System.currentTimeMillis() - pidStartTime));
    		shooter.clearIAccumulation();
    		conveyor.setSetpoint(SmartDashboard.getNumber("Conveyor Setpoint", 200));
    	}else{
    		intake.setPower(0.50);
    		shooter.setSetpoint(desiredBackSetpoint, desiredFrontSetpoint);
            conveyor.setSetpoint(SmartDashboard.getNumber("Conveyor Setpoint", 250));
	    	if (System.currentTimeMillis() - startTime >= SmartDashboard.getNumber("Shooter Gate Time", gateTime)) {
		    		
//	    		shooterGate.setAllSolenoids(ShooterGateSubsystem.GATE_CLOSE);
	    		//shooterGate.gatesClose();
	    		
	    		if(shooter.onTarget()) {
	    			//System.out.println("on target");
		    		if (counter % 2 == 0) {
		    			if(shooter.onTarget()){
		    				shooterGate.setSolenoid(1, ShooterGateSubsystem.GATE_OPEN);
		    			}
		    		}
		    		else {
		    			if(shooter.onTarget()){
		    				shooterGate.setSolenoid(2, ShooterGateSubsystem.GATE_OPEN);
		    			}
		    		}
		    		startTime = System.currentTimeMillis();
		    		counter++;
		    	}
		    	else {
	//	    		conveyor.setMotorPower(0.0);
		    	}
	    	}
		    	
	    	if(System.currentTimeMillis() - startTime >= SmartDashboard.getNumber("Gate Open Time", endTime)){
	    		//shooterGate.gatesClose();
	    	}
    	}
			// Logging
		if(print != null) {
			print.format("%.4f, %.4f, %.4f, %.4f, %b, %b\n",
					desiredFrontSetpoint, shooter.getFrontRPS(), 
					desiredBackSetpoint, shooter.getBackRPS(), 
					shooterGate.getSolenoidState(0), shooterGate.getSolenoidState(1));
			//System.out.println("LOG");
		}
		
		if(System.currentTimeMillis() - pidStartTime > 2000){
			shooterGate.setSolenoid(1, ShooterGateSubsystem.GATE_OPEN);
		}
		
		if(System.currentTimeMillis() - pidStartTime > 2350){
			shooterGate.setSolenoid(2, ShooterGateSubsystem.GATE_OPEN);
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
    	Robot.getCompressor().start();
    	intake.setPower(0.0);
    	if(print != null) {
			print.flush();
			print.close();
		}
    }

    protected void interrupted() {
    	end();
    }
}
