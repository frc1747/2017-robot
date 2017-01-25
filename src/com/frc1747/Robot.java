package com.frc1747;

import com.frc1747.subsystems.Climber;
import com.frc1747.subsystems.Conveyor;
import com.frc1747.subsystems.Drivetrain;
import com.frc1747.subsystems.Intake;
import com.frc1747.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends IterativeRobot {

	private static Drivetrain drivetrain;
	private static Shooter shooter;
	private static Conveyor conveyor;
	private static Climber climber;
	private static OI oi;
	private static Intake intake;
	
	public static Intake getIntake(){
		return intake;
	}
	public static OI getOI(){
		return oi;
	}
	public static Drivetrain getDrivetrain() {
		return drivetrain;
	}
	public static Shooter getShooter() {
		return shooter;
	}
	public static Conveyor getConveyor() {
		return conveyor;
	}
	public static Climber getClimber() {
		return climber;
	}
	
	@Override
	public void robotInit() {
		drivetrain = new Drivetrain();
		shooter = new Shooter();
		conveyor = new Conveyor();
		climber = new Climber();
		intake = new Intake();
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

