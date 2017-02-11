package com.frc1747.subsystems;

import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ShooterGateSubsystem extends Subsystem {
	private Solenoid solenoid1;
	private Solenoid solenoid2;
	private Solenoid solenoid3; 
	
	private static ShooterGateSubsystem instance;
	
	public final static boolean GATE_CLOSE = true;
	public final static boolean GATE_OPEN = false;
	
	public ShooterGateSubsystem() {
		solenoid1 = new Solenoid(RobotMap.SHOOTER_SOLENOID1);
		solenoid2 = new Solenoid(RobotMap.SHOOTER_SOLENOID2);
		solenoid3 = new Solenoid(RobotMap.SHOOTER_SOLENOID3);
		
	}
	public void setSolenoid1(boolean solenoidState){
		solenoid1.set(solenoidState);
	}
	public void setSolenoid2(boolean solenoidState){
		solenoid2.set(solenoidState);
	}
	public void setSolenoid3(boolean solenoidState){
		solenoid3.set(solenoidState);
	}
	
	public static ShooterGateSubsystem getInstance(){
		if (instance == null){
			instance = new ShooterGateSubsystem();
		}
		return instance;
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

