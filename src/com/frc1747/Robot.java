package com.frc1747;

import java.util.logging.Level;

import lib.frc1747.subsystems.HBRSubsystem;
import lib.frc1747.controller.Logitech;
import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

import com.frc1747.commands.UpdateDashboard;
import com.frc1747.commands.auton.AutonTemplate;
import com.frc1747.commands.gear.GearMechClose;
import com.frc1747.subsystems.ClimbSubsystem;
import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.GearSubsystem;
import com.frc1747.subsystems.ShifterSubsystem;
import com.frc1747.subsystems.ShooterGateSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	static Compressor compressor;
	
	Command auton;
	DriveSubsystem drive;
	boolean aButtonState, bButtonState;
	
	Logger logger;
	
	Autons[] modes;
	int index;

	@Override
	public void robotInit() {
		logger = Instrumentation.getLogger("Robot");
		logger.log(Level.INFO, "Robot Init");
		logger.registerDouble("Battery voltage", false, true);
		
		(new UpdateDashboard()).start();
		initSubsystems();
		SmartDashboard.putNumber("Auton Shoot Time", 5000);
		
		// Initialize the auton chooser
		aButtonState = false;
		bButtonState = false;
		
		modes = Autons.class.getEnumConstants();
		index = 1;
    	logger.registerString("Selected Auton", true, false);
    	logger.putString("Selected Auton", modes[index].toString());
	}

	@Override
	public void teleopInit() {
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().add(new GearMechClose());
		//System.out.println(SmartDashboard.getNumber("Test", 0));
		ShifterSubsystem.getInstance().enableAutoshifting();
		drive = DriveSubsystem.getInstance();
		drive.debug();
		logger.enableLogging();
	}

	@Override
	public void autonomousInit() {
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().add(new GearMechClose());
		ShifterSubsystem.getInstance().disableAutoshifting();
		//(auton = new AutonTemplate(autonChoice.getSelected())).start();
		(auton = new AutonTemplate(modes[index])).start();
		logger.enableLogging();
	}
	
	@Override
	public void disabledInit() {
		logger.disableLogging();
		logger.flushAll();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		HBRSubsystem.update();
		logger.putDouble("Battery voltage", DriverStation.getInstance().getBatteryVoltage());
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
//		System.out.println(drive.getAveragePosition());
//		System.out.println(SmartDashboard.getNumber("Test",0));
		HBRSubsystem.update();
		logger.putDouble("Battery voltage", DriverStation.getInstance().getBatteryVoltage());
	}
	
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		HBRSubsystem.update();
		
		// Auton chooser
		if(!aButtonState && OI.getInstance().getOperator().getButton(Logitech.A).get()){
			nextAuton();
		}
		aButtonState = OI.getInstance().getOperator().getButton(Logitech.A).get();
		if(!bButtonState && OI.getInstance().getOperator().getButton(Logitech.B).get()){
			prevAuton();
		}
		bButtonState = OI.getInstance().getOperator().getButton(Logitech.B).get();
		
    	logger.putString("Selected Auton", modes[index].toString());
    	logger.putDouble("Battery voltage", DriverStation.getInstance().getBatteryVoltage());
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
		GearSubsystem.getInstance();
	}
	
	public enum Autons {
		NONE, HOPPER1, HOPPER2, GEAR_LEFT, GEAR_RIGHT, JUST_SHOOT, CENTER_GEAR, TEST_AUTON, TEST_AUTON_2
	}
	
	public static Compressor getCompressor(){
		if(compressor == null){
			compressor = new Compressor();
		}
		return compressor;
	}
	
	// Auton Chooser handling
    public void nextAuton() {
    	logger.log(Level.INFO, "***NEXT AUTON***");
    	index++;
    	if(index >= modes.length) index -= modes.length;
    }
    
    public void prevAuton() {
    	logger.log(Level.INFO, "***PREV AUTON***");
    	index--;
    	if(index < 0) index += modes.length;
    }
}

