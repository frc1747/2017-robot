package com.frc1747.subsystems;

import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ShifterSubsystem extends Subsystem {

	public Solenoid shifter;
	private static ShifterSubsystem instance;
	
	public final boolean HIGH_GEAR = true;
	public final boolean LOW_GEAR = false;
	
	private ShifterSubsystem() {
		shifter = new Solenoid(RobotMap.SHIFT_SOLENOID);
	}
	public static ShifterSubsystem getInstance() {
		return instance == null ? instance = new ShifterSubsystem() : instance;
	}
	
	public void setTransmission(boolean gear){
		shifter.set(gear);
	}
	
	public boolean isHighGear(){
		return shifter.get() == HIGH_GEAR;
	}
	
	public boolean isLowGear(){
		return shifter.get() == LOW_GEAR;
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

