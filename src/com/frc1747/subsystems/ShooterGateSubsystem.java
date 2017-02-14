package com.frc1747.subsystems;

import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class ShooterGateSubsystem extends HBRSubsystem {
	
	private Solenoid [] solenoids = new Solenoid[3];
	
	private static ShooterGateSubsystem instance;
	
	public final static boolean GATE_CLOSE = true;
	public final static boolean GATE_OPEN = false;
	
	public ShooterGateSubsystem() {
		solenoids[0] = new Solenoid(RobotMap.SHOOTER_SOLENOID1);
		solenoids[1] = new Solenoid(RobotMap.SHOOTER_SOLENOID2);
		solenoids[2] = new Solenoid(RobotMap.SHOOTER_SOLENOID3);
	}
	
	/**
	 * Set the state of a solenoid
	 * @param solNum Solenoid number where solNum > 0
	 * @param solenoidState true (Close), false (Open)
	 */
	public void setSolenoid(int solNum, boolean solenoidState) {
		solenoids[solNum - 1].set(solenoidState);
	}
	
	public void setAllSolenoids(boolean solenoidState) {
		setSolenoid(1, solenoidState);
		setSolenoid(2, solenoidState);
		setSolenoid(3, solenoidState);
	}
	
	public static ShooterGateSubsystem getInstance(){
		return instance == null ? instance = new ShooterGateSubsystem() : instance;
	}
	
    public void initDefaultCommand() {
    	
    }
    
	@Override
	public void updateDashboard() {
		
	}
}

