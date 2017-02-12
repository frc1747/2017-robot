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
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
