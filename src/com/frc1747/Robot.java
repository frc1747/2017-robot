package com.frc1747;

import com.frc1747.subsystems.Drivetrain;
import com.frc1747.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends IterativeRobot {

	public static Drivetrain drivetrain;
	public static Shooter shooter;
	
	@Override
	public void robotInit() {
		drivetrain = new Drivetrain();
		shooter = new Shooter();
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
	
	public static Drivetrain getDrivetrain(){
		return drivetrain;
	}
	
	public static Shooter getShooter(){
		return shooter;
	}
	
	
}

