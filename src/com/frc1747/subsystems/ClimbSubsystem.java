package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimbSubsystem extends Subsystem {

	private CANTalon motor1;
	private CANTalon motor2;
	private static ClimbSubsystem instance;
	
	public final double CLIMBER_POWER = 0; //TODO: use actual power
	
	private ClimbSubsystem() {
		
		motor1 = new CANTalon(RobotMap.CLIMBER_MOTOR1);
		motor2 = new CANTalon(RobotMap.CLIMBER_MOTOR2);
		
	}
	
	public static ClimbSubsystem getInstance(){
		if (instance == null){
			instance = new ClimbSubsystem();
		}
		return instance;
	}
	
	public void setMotorPower(double power) {
		
		motor1.set(power);
		motor2.set(power);
		
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
