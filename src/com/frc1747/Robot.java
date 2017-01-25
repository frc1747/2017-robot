package com.frc1747;

import com.frc1747.subsystems.ClimbSubsystem;
import com.frc1747.subsystems.ConveyorSubsystem;
import com.frc1747.subsystems.DriveSubsystem;
import com.frc1747.subsystems.CollectorSubsystem;
import com.frc1747.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends IterativeRobot {

	private static DriveSubsystem drivetrain;
	private static ShooterSubsystem shooter;
	private static ConveyorSubsystem conveyor;
	private static ClimbSubsystem climber;
	private static OI oi;
	private static CollectorSubsystem intake;
	
	public static CollectorSubsystem getIntake(){
		return intake;
	}
	public static OI getOI(){
		return oi;
	}
	public static DriveSubsystem getDrivetrain() {
		return drivetrain;
	}
	public static ShooterSubsystem getShooter() {
		return shooter;
	}
	public static ConveyorSubsystem getConveyor() {
		return conveyor;
	}
	public static ClimbSubsystem getClimber() {
		return climber;
	}
	
	@Override
	public void robotInit() {
		drivetrain = new DriveSubsystem();
		shooter = new ShooterSubsystem();
		conveyor = new ConveyorSubsystem();
		climber = new ClimbSubsystem();
		intake = new CollectorSubsystem();
		oi = new OI();
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

