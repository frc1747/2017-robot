package com.frc1747.commands.auton;

import com.frc1747.commands.collector.Extend;
import com.frc1747.commands.collector.Retract;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */ 
public class AutonSetup extends CommandGroup {

    public AutonSetup() {
    	addSequential(new Extend());
    	addSequential(new Retract());
    		
    }
}
