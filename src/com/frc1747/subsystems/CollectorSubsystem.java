package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class CollectorSubsystem extends HBRSubsystem {

	CANTalon motor;
	
	private static CollectorSubsystem instance;
	
	private CollectorSubsystem(){
		motor = new CANTalon(RobotMap.INTAKE_MOTOR);
	}
	
	public static CollectorSubsystem getInstance(){
		if (instance == null){
			instance = new CollectorSubsystem();
		}
		
		return instance;
	}
	
	public void in(double power){
		//possibly the wrong direction
		motor.set(power);
	}
	
	public void out(double power){
		//possibly the wrong direction
		motor.set(-power);
	}

    public void initDefaultCommand() {
      
    }

	@Override
	public void updateDashboard() {
		// TODO Auto-generated method stub
		
	}
}

