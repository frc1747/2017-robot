package com.frc1747.commands;

import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoShoot extends CommandGroup {
	
    public AutoShoot() {
    	addSequential(new AutoAllign());
    	addSequential(new Shoot());
    }
}
