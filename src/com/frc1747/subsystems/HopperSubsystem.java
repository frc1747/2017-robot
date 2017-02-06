package com.frc1747.subsystems;

import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class HopperSubsystem extends Subsystem {

	private Solenoid sol1;
	public boolean OUT = true, IN = false;
	
	public HopperSubsystem() {
		
		sol1 = new Solenoid(RobotMap.HOPPER_SOLENOID);
		
	}
	
	public void setPosition(boolean position) {
		sol1.set(position);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
