package com.frc1747.subsystems;

import com.frc1747.RobotMap;
import com.frc1747.commands.collector.TakeIn;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.speed_controller.HBRTalon;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class CollectorSubsystem extends HBRSubsystem {

	public final double 
		COLLECTOR_POWER = 0.85, //previously .85
		WHEEL_DIAMETER = 1.6/12, //in feet
		WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI,
		ENCODER_COUNTS_PER_REVOLUTION = 4,
		ENCODER_REFRESH_TIME = .1; //in seconds, motor speed is recorded over intervals of this
	
	public final Value
		EXTEND_POSITION = DoubleSolenoid.Value.kForward,
		RETRACT_POSITION = DoubleSolenoid.Value.kReverse,
		HOLD_POSITION = DoubleSolenoid.Value.kOff;
	
	private HBRTalon motor;
	private DoubleSolenoid intakeSolenoid;
	
	private static CollectorSubsystem instance;
	
	private CollectorSubsystem(){
		
		motor = new HBRTalon(RobotMap.INTAKE_MOTOR);
		motor.setInverted(RobotMap.INTAKE_INVERTED);
		motor.setScaling(4 * ENCODER_COUNTS_PER_REVOLUTION);
		
		intakeSolenoid = new DoubleSolenoid(RobotMap.INTAKE_SOLENOID_PORT_1, RobotMap.INTAKE_SOLENOID_PORT_2);
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
	
	public double getSpeed(){
		return motor.getSpeed();
	}
	
	public double getSpeedFeetPerSecond(){
		return getSpeed() * WHEEL_CIRCUMFERENCE  
				/*/ (ENCODER_COUNTS_PER_REVOLUTION * ENCODER_REFRESH_TIME)*/;
	}
	
	public void setPosition(Value position) {
		intakeSolenoid.set(position);
	}
	
	public boolean isIntakeOut(){
		return intakeSolenoid.get() == EXTEND_POSITION;
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

