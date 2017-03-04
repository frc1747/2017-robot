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

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	static Compressor compressor;
	
	Command auton;
	SendableChooser<Autons> autonChoice;

	@Override
	public void robotInit() {
//		SendableChooser autoChooser = new SendableChooser();
//		autoChooser.addDefault("Position 1", object);
		
		(new UpdateDashboard()).start();
		initSubsystems();
		SmartDashboard.putNumber("Auton Shoot Time", 5000);
		
		autonChoice = new SendableChooser<>();
		autonChoice.addObject("Correct gear order", Autons.GEAR_LEFT);
		autonChoice.addObject("reversed gear order", Autons.GEAR_RIGHT);
		autonChoice.addObject("Red Hopper", Autons.HOPPER_RED);
		autonChoice.addObject("Blu Hopper", Autons.HOPPER_BLUE);
		SmartDashboard.putData("Auton profile", autonChoice);
	}

	@Override
	public void teleopInit() {
		Scheduler.getInstance().removeAll();
		System.out.println(SmartDashboard.getNumber("Test", 0));
		ShifterSubsystem.getInstance().enableAutoshifting();
	}

	@Override
	public void autonomousInit() {
		Scheduler.getInstance().removeAll();
		ShifterSubsystem.getInstance().disableAutoshifting();
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
//		System.out.println(SmartDashboard.getNumber("Test",0));
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
	
	public enum Autons {
		HOPPER_BLUE, HOPPER_RED, GEAR_LEFT, GEAR_RIGHT
	}
	
	public static Compressor getCompressor(){
		if(compressor == null){
			compressor = new Compressor();
		}
		return compressor;
	}
}

