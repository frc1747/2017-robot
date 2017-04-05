package com.frc1747;

import java.util.logging.Level;

import lib.frc1747.subsystems.HBRSubsystem;
import lib.frc1747.commands.AutonChooser;
import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;
import lib.frc1747.motion_profile.generator._1d.ProfileGenerator;

import com.frc1747.commands.UpdateDashboard;
import com.frc1747.commands.auton.AutonTemplate;
import com.frc1747.commands.drive.DriveProfile;
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
	DriveSubsystem drive;
	
	Logger logger;

	@Override
	public void robotInit() {
//		SendableChooser autoChooser = new SendableChooser();
//		autoChooser.addDefault("Position 1", object);
		
		logger = Instrumentation.getLogger("Robot");
		logger.log(Level.INFO, "Robot Init");
		
		(new UpdateDashboard()).start();
		initSubsystems();
		SmartDashboard.putNumber("Auton Shoot Time", 5000);
		
		autonChoice = new SendableChooser<>();
//		autonChoice.addObject("Correct gear order", Autons.GEAR_LEFT);
//		autonChoice.addObject("reversed gear order", Autons.GEAR_RIGHT);
		autonChoice.addObject("Hopper", Autons.HOPPER);
		SmartDashboard.putData("Auton profile", autonChoice);
		
	}

	@Override
	public void teleopInit() {
		Scheduler.getInstance().removeAll();
		//System.out.println(SmartDashboard.getNumber("Test", 0));
		ShifterSubsystem.getInstance().enableAutoshifting();
		drive = DriveSubsystem.getInstance();
		drive.debug();
	}

	@Override
	public void autonomousInit() {
		Scheduler.getInstance().removeAll();
		ShifterSubsystem.getInstance().disableAutoshifting();
		//(auton = new AutonTemplate(autonChoice.getSelected())).start();
		(auton = new AutonTemplate((Autons)(OI.getInstance().getSelectedAuton()))).start();
	}
	
	@Override
	public void disabledInit() {
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		HBRSubsystem.update();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
//		System.out.println(drive.getAveragePosition());
//		System.out.println(SmartDashboard.getNumber("Test",0));
		HBRSubsystem.update();
	}
	
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		HBRSubsystem.update();
		logger.log(Level.INFO, "*************Test**************");
		//System.out.println(OI.getInstance().getDriver().getButton(3).get());
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
		HBRSubsystem.update();
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
		NONE, HOPPER, GEAR_LEFT, GEAR_RIGHT
	}
	
	public static Compressor getCompressor(){
		if(compressor == null){
			compressor = new Compressor();
		}
		return compressor;
	}
}

