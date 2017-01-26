package com.frc1747.commands;

import com.frc1747.OI;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Increment extends Command {
	
	int total;

    public Increment() {

    	total = 0;
    }

    protected void initialize() {
    	total++;
    	SmartDashboard.putNumber("increment", total);
    }

    protected void execute() {
    	
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
