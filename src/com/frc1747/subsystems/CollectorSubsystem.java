package com.frc1747.subsystems;

import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import lib.frc1747.speed_controller.HBRTalon;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class CollectorSubsystem extends HBRSubsystem {

	public final double 
		COLLECTOR_POWER = 0.85, //TODO: put actual value we want to use
		WHEEL_DIAMETER = 1.6/12, //in feet
		WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI,
		ENCODER_COUNTS_PER_REVOLUTION = 4,
		ENCODER_REFRESH_TIME = .1; //in seconds, motor speed is recorded over intervals of this
	
	public final boolean
		EXTEND_POSITION = true,
		RETRACT_POSITION = false;
	
	private HBRTalon motor;
	private Solenoid intakeSolenoid;
	
	private static CollectorSubsystem instance;
	
	private CollectorSubsystem(){
		
		motor = new HBRTalon(RobotMap.INTAKE_MOTOR);
		motor.setInverted(RobotMap.INTAKE_INVERTED);
		motor.setScaling(ENCODER_COUNTS_PER_REVOLUTION*ENCODER_REFRESH_TIME);
		
		intakeSolenoid = new Solenoid(RobotMap.INTAKE_SOLENOID);
	}
	
	public static CollectorSubsystem getInstance(){
		if (instance == null){
			instance = new CollectorSubsystem();
		}
		return instance;
	}
	
	public void setPower(double power){
		motor.set(power);
	}
	
	public void IntakeUp(){
		intakeSolenoid.set(true);
	}
	
	public void IntakeDown(){
		intakeSolenoid.set(false);
	}
	
	public double getSpeed(){
		return motor.getSpeed();
	}
	
	public double getSpeedFeetPerSecond(){
		return getSpeed() * WHEEL_CIRCUMFERENCE  
				/*/ (ENCODER_COUNTS_PER_REVOLUTION * ENCODER_REFRESH_TIME)*/;
	}
	
	public void setPosition(boolean position) {
		intakeSolenoid.set(position);
	}

    public void initDefaultCommand() {
    }

	@Override
	public void updateDashboard() {
		super.updateDashboard();
	}

	@Override
	public void debug() {
		
	}
}

