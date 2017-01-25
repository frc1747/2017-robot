package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.frc1747.RobotMap;

import lib.frc1747.subsystems.HBRSubsystem;

public class Conveyor extends HBRSubsystem {

	private CANTalon motor1;
	
	public Conveyor() {
		
		motor1 = new CANTalon(RobotMap.CONVEYOR_MOTOR);
		
	}
	
	public void setMotorPower(double power) {
		
		motor1.set(power);
		
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDashboard() {
		// TODO Auto-generated method stub
		
	}

}
