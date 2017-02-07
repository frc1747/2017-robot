package com.frc1747.commands.hopper;

import com.frc1747.subsystems.HopperSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Retract extends Command {

	private HopperSubsystem hopper;
	
    public Retract() {
    	requires(hopper);
    }

    protected void initialize() {
    }

    protected void execute() {
    	hopper.setPosition(hopper.IN);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
