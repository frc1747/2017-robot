package com.frc1747;

import com.frc1747.commands.UpdateDashboard;
import com.frc1747.commands.drive.DriveProfile;
import com.frc1747.subsystems.AutonTemplate;
import com.frc1747.subsystems.ClimbSubsystem;
import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.ShifterSubsystem;
import com.frc1747.subsystems.ShooterGateSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	
	Command auton;
	SendableChooser<Integer> autonChoice;

	@Override
	public void robotInit() {
//		SendableChooser autoChooser = new SendableChooser();
//		autoChooser.addDefault("Position 1", object);
		
		(new UpdateDashboard()).start();
		initSubsystems();
		SmartDashboard.putNumber("Auton Shoot Time", 5000);
		
		autonChoice = new SendableChooser<>();
		autonChoice.addObject("Correct gear order", 5);
		autonChoice.addObject("reversed gear order", 6);
		SmartDashboard.putData("Auton profile", autonChoice);
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void autonomousInit() {
		(auton = new AutonTemplate(autonChoice.getSelected())).start();
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
		ShooterGateSubsystem.getInstance();
		OI.getInstance();
		ShifterSubsystem.getInstance();
	}
}

