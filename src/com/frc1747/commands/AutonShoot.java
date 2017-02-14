package com.frc1747.commands;

import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutonShoot extends CommandGroup {
	double shootTime;
	long startTime;

    public AutonShoot() {
    	shootTime = SmartDashboard.getNumber("Auton Shoot Time", 5000);
    	
    	
    	addSequential(new AutoAllign());
    	startTime = System.currentTimeMillis();
    	while(System.currentTimeMillis() - startTime <= shootTime){
    	addSequential(new Shoot());
    	}
    }
}
