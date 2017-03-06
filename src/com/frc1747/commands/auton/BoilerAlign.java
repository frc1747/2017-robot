package com.frc1747.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BoilerAlign extends CommandGroup {

    public BoilerAlign() {
    	//addSequential(new BoilerHorizontalAutoAlign());
    	addSequential(new BoilerVerticalAutoAlign());
    }
}
