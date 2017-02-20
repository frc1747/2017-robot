package com.frc1747.subsystems;

import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class ShooterGateSubsystem extends HBRSubsystem {
	
	private DoubleSolenoid [] solenoids = new DoubleSolenoid[2];
	
	private static ShooterGateSubsystem instance;
	
	public final static Value GATE_CLOSE = DoubleSolenoid.Value.kReverse;
	public final static Value GATE_OPEN = DoubleSolenoid.Value.kForward;
	public final static Value GATE_HOLD = DoubleSolenoid.Value.kOff;
	
	public ShooterGateSubsystem() {
		solenoids[0] = new DoubleSolenoid(RobotMap.GATE_SOLENOID1_PORT_1, RobotMap.GATE_SOLENOID1_PORT_2);
		solenoids[1] = new DoubleSolenoid(RobotMap.GATE_SOLENOID2_PORT_1, RobotMap.GATE_SOLENOID2_PORT_2);
	}
	
	/**
	 * Set the state of a solenoid
	 * @param solNum Solenoid number where solNum > 0
	 * @param solenoidState true (Close), false (Open)
	 */
	public void setSolenoid(int solNum, Value solenoidState) {
		solenoids[solNum - 1].set(solenoidState);
	}
	
	public void setAllSolenoids(Value solenoidState) {
		setSolenoid(1, solenoidState);
		setSolenoid(2, solenoidState);
	}
	
	public static ShooterGateSubsystem getInstance(){
		return instance == null ? instance = new ShooterGateSubsystem() : instance;
	}
	public void gatesOpen(){
		solenoids[1].set(GATE_OPEN);	
		solenoids[0].set(GATE_OPEN);
	}
	public void gatesClose(){
		solenoids[1].set(GATE_CLOSE);	
		solenoids[0].set(GATE_CLOSE);
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

