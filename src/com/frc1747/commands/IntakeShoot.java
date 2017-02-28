package com.frc1747.commands;

import com.frc1747.commands.collector.TakeIn;
import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeShoot extends CommandGroup {

    public IntakeShoot() {
        addSequential(new TakeIn());
        addParallel(new Shoot());
    }
}
