package com.frc1747;

import com.frc1747.subsystems.Conveyor;
import com.frc1747.subsystems.Drivetrain;
import com.frc1747.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends IterativeRobot {

	private static Drivetrain drivetrain;
	private static Shooter shooter;
	private static Conveyor conveyor;
	
	public static Drivetrain getDrivetrain() {
		return drivetrain;
	}
	public static Shooter getShooter() {
		return shooter;
	}
	public static Conveyor getConveyor() {
		return conveyor;
	}
	
	@Override
	public void robotInit() {
		drivetrain = new Drivetrain();
		shooter = new Shooter();
		conveyor = new Conveyor();
	}

	@Override
	public void teleopInit() {
	
	}

	@Override
	public void autonomousInit() {
	
	}
	
	@Override
	public void disabledInit() {
	
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

