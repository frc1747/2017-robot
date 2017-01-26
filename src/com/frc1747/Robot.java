package com.frc1747;

import com.frc1747.commands.UpdateDashboard;
import com.frc1747.subsystems.ClimbSubsystem;
import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends IterativeRobot {

	@Override
	public void robotInit() {
		(new UpdateDashboard()).start();

		initSubsystems();
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
	
	public void initSubsystems() {
		ClimbSubsystem.getInstance();
		CollectorSubsystem.getInstance();
		ConveyorSubsystem.getInstance();
		DriveSubsystem.getInstance();
		ShooterSubsystem.getInstance();
	}
}

