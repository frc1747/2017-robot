package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class CollectorSubsystem extends HBRSubsystem {

	CANTalon motor;
	Solenoid intakeSolenoid;
	
	private static CollectorSubsystem instance;
	
	private CollectorSubsystem(){
		motor = new CANTalon(RobotMap.INTAKE_MOTOR);
		intakeSolenoid = new Solenoid(RobotMap.INTAKE_SOLENOID);
	}
	
	public static CollectorSubsystem getInstance(){
		if (instance == null){
			instance = new CollectorSubsystem();
		}
		
		return instance;
	}
	
	public void in(double power){
		// TODO possibly the wrong direction
		motor.set(power);
	}
	
	public void out(double power){
		// TODO possibly the wrong direction
		motor.set(-power);
	}
	
	public void IntakeUp(){
		// TODO possibly the wrong direction
		intakeSolenoid.set(true);
	}
	
	public void IntakeDown(){
		// TODO possibly the wrong direction
		intakeSolenoid.set(false);
	}

    public void initDefaultCommand() {
      
    }

	@Override
	public void updateDashboard() {
		// TODO Auto-generated method stub
		
	}
}

