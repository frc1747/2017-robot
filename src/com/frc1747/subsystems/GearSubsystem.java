package com.frc1747.subsystems;

import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearSubsystem extends Subsystem {

	public final boolean OPEN = true;
	public final boolean CLOSED = false;
	
	public Solenoid gear;
	
	private static GearSubsystem instance;
	
	public GearSubsystem(){
		gear = new Solenoid(RobotMap.GEAR_SOLENOID_PORT);
	}
	
	public void openGearMech(){
		gear.set(OPEN);
	}
	
	public void closeGearMech(){
		gear.set(CLOSED);
	}
	
	public static GearSubsystem getInstance(){
		return instance == null ? instance = new GearSubsystem() : instance;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

