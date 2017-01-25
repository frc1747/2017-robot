package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import lib.frc1747.subsystems.HBRSubsystem;

/*
 * This is a test subsystem. This does not do anything.
 * This is just here to make a sample subsystem.
 */
public class Shooter extends HBRSubsystem {

	CANTalon topShooterMotor, bottomShooterMotor;
	
    public Shooter(){
    	topShooterMotor = new CANTalon(RobotMap.TOP_SHOOTER_MOTOR);
    	bottomShooterMotor = new CANTalon(RobotMap.BOTTOM_SHOOTER_MOTOR);
    }
    
    
	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

	@Override
	public void updateDashboard() {
		// Make update smart dashboard calls here.
	}
}

