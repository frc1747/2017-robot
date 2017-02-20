package com.frc1747.subsystems;

import com.frc1747.RobotMap;
import com.frc1747.commands.shifter.AutoShift;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class ShifterSubsystem extends HBRSubsystem {

	private static final int SHIFT_ACCELERATION_HIGH = 1;
	private static final int SHIFT_VELOCITY_LOW = 1;
	private static final double TURNING_THRESHOLD_TO_SHIFT = 1;
	public final boolean HIGH_GEAR = true;
	public final boolean LOW_GEAR = false;
	
	public Solenoid shifter;
	//private DriveSubsystem drive;
	
	private static ShifterSubsystem instance;
	
	private ShifterSubsystem() {
		shifter = new Solenoid(RobotMap.SHIFT_SOLENOID);
		//drive = DriveSubsystem.getInstance();
 	}
	public static ShifterSubsystem getInstance() {
		return instance == null ? instance = new ShifterSubsystem() : instance;
	}
	
	public void setTransmission(boolean gear){
		System.out.println("shift set: " + gear);
		shifter.set(gear);
	}
	public boolean shouldShiftUp(){
		
		/*double slope = -SHIFT_ACCELERATION_HIGH/SHIFT_VELOCITY_LOW;
		
		
		if(Math.abs(drive.getTurning()) <= TURNING_THRESHOLD_TO_SHIFT) {
			//TODO: Might need to be XAcceleration/YAcceleration/Combination of the two
			if (drive.getForwardAcceleration() >= LOWER_THRESHOLD && drive.getForwardAcceleration() <= UPPER_THRESHOLD) {
				return getGear();
			}
			else if(drive.getForwardAcceleration() >= slope*drive.getSpeed() + SHIFT_ACCELERATION_HIGH) {
				return HIGH_GEAR;
			} else {
				return LOW_GEAR;
			}
		} else {
			return LOW_GEAR;
		}*/

		
		return false;
		
	}
	
	public boolean isHighGear(){
		return shifter.get() == HIGH_GEAR;
	}
	
	public boolean isLowGear(){
		return shifter.get() == LOW_GEAR;
	}
	
	public boolean getGear(){
		return shifter.get();
	}
	
    public void initDefaultCommand() {
    	//setDefaultCommand(new AutoShift());
    }
	@Override
	public void updateDashboard() {
		super.updateDashboard();
	}
	
	@Override
	public void debug() {
		SmartDashboard.putBoolean("Gear state", getGear());
	}
}

