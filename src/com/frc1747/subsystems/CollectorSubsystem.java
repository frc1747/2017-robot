package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.frc1747.RobotMap;
import com.frc1747.commands.collector.TakeIn;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class CollectorSubsystem extends HBRSubsystem {

	CANTalon motor;
	Solenoid intakeSolenoid;
	
	public final double COLLECTOR_POWER = 0.85; //TODO: put actual value we want to use
	
	private final double WHEEL_DIAMETER = 1.6/12; //in feet
	private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; 
	private final double ENCODER_COUNTS_PER_REVOLUTION = 4;
	private final double ENCODER_REFRESH_TIME = .1; //in seconds, motor speed is recorded over intervals of this
	public final boolean EXTEND_POSITION = true;
	public final boolean RETRACT_POSITION = false;
	
	private static CollectorSubsystem instance;
	
	private CollectorSubsystem(){
		motor = new CANTalon(RobotMap.INTAKE_MOTOR);
		intakeSolenoid = new Solenoid(RobotMap.INTAKE_SOLENOID);
		System.out.println("collectorsubsystem contructor");
	}
	
	public static CollectorSubsystem getInstance(){
		if (instance == null){
			instance = new CollectorSubsystem();
		}
		
		return instance;
	}
	
	public void setPower(double power){
		// TODO possibly the wrong direction
		motor.set(power);
	}
	
	public void IntakeUp(){
		// TODO possibly the wrong direction
		intakeSolenoid.set(true);
	}
	
	public void IntakeDown(){
		// TODO possibly the wrong direction
		intakeSolenoid.set(false);
	}
	
	public double getSpeed(){
		return motor.getSpeed();
	}
	
	public double getSpeedFeetPerSecond(){
		return getSpeed()*WHEEL_CIRCUMFERENCE/(ENCODER_COUNTS_PER_REVOLUTION*ENCODER_REFRESH_TIME);
	}
	
	public void setPosition(boolean position) {
		intakeSolenoid.set(position);
	}

    public void initDefaultCommand() {
    	//setDefaultCommand(new TakeIn());
    }

	@Override
	public void updateDashboard() {
		// TODO Auto-generated method stub
		
	}
}

