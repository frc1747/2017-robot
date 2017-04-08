package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.frc1747.RobotMap;
import com.frc1747.commands.climb.Climb;

import lib.frc1747.subsystems.HBRSubsystem;

public class ClimbSubsystem extends HBRSubsystem {

	private CANTalon motor1;
	//private CANTalon motor2;
	private static ClimbSubsystem instance;
	
	public final double CLIMBER_POWER = 1.0;
	
	private ClimbSubsystem() {
		motor1 = new CANTalon(RobotMap.CLIMBER_MOTOR1);
		//motor2 = new CANTalon(RobotMap.CLIMBER_MOTOR2);
		motor1.setInverted(RobotMap.CLIMBER_INVERTED1);
		//motor2.setInverted(RobotMap.CLIMBER_INVERTED2);
	}
	
	public static ClimbSubsystem getInstance(){
		if (instance == null){
			instance = new ClimbSubsystem();
		}
		return instance;
	}
	
	public void setMotorPower(double power) {
		motor1.set(power);
		//motor2.set(power);
	}
	public double getVoltage(){
		return motor1.getOutputVoltage();
	}
	public double getCurrent(){
		return motor1.getOutputCurrent();
	}
	public double getBusVoltage(){
		return motor1.getBusVoltage();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Climb());
	}

	@Override
	public void updateDashboard() {
		super.updateDashboard();
	}

	@Override
	public void debug() {
		
	}

	@Override
	public double[][] pidRead() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pidWrite(double[] output) {
		// TODO Auto-generated method stub
		
	}
}
