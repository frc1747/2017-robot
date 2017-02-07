package com.frc1747.commands.hopper;

import com.frc1747.subsystems.HopperSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Extend extends Command {

	private HopperSubsystem hopper;
	
    public Extend() {
    	requires(hopper);
    }

    protected void initialize() {
    }

    protected void execute() {
    	hopper.setPosition(hopper.OUT);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
