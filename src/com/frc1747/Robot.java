package com.frc1747;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends IterativeRobot {

	private Solenoid sol, sol2;
	
	
	@Override
	public void robotInit() {
		sol = new Solenoid(0);
		sol2 = new Solenoid(1);
	}

	@Override
	public void teleopInit() {
		sol.set(true);
		sol2.set(false);
	}

	@Override
	public void autonomousInit() {
	
	}
	
	@Override
	public void disabledInit() {
		sol.set(false);
		sol2.set(true);
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}
	
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}

