package com.frc1747.commands.auton;

import java.util.logging.Level;

import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

import com.frc1747.Robot.Autons;
import com.frc1747.commands.AutonAlign;
import com.frc1747.commands.Delay;
import com.frc1747.commands.drive.DriveProfile;
import com.frc1747.commands.drive.DriveProfile2;
import com.frc1747.commands.drive.Rotate;
import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTemplate extends CommandGroup{
	public AutonTemplate(Autons auton){
		Logger logger = Instrumentation.getLogger("Auton Template");
		logger.log(Level.INFO, "Running auton: " + auton.toString());
		
		Alliance alliance = DriverStation.getInstance().getAlliance();
		logger.log(Level.INFO, "Alliance: " + alliance.toString());
		
		switch(auton){
		case HOPPER1:
			addParallel(new AutonExtend());
			//these two run in parallel
			if(alliance == Alliance.Red){
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper3a_red.csv"));
				addSequential(new Delay(1250));
				addParallel(new AutonBallClear());
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper3b_red.csv"));
				addSequential(new Rotate(42));
			}
			else {
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper3a_blue.csv"));
				addSequential(new Delay(1250));
				addParallel(new AutonBallClear());
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper3b_blue.csv"));
				addSequential(new Rotate(-42));
			}
			addSequential(new Delay(750));
			addParallel(new AutonAlign(alliance == Alliance.Red));
			addSequential(new Shoot());
			break;
		case HOPPER2:
			addSequential(new AutonStopMotors());
			if(alliance == Alliance.Red) {
				addSequential(new DriveProfile2("/home/lvuser/hopper4_red.csv"));
				addSequential(new AutonStopMotors());
				addParallel(new AutonExtend());
				addSequential(new AutonDriveTurn(0.001, 40));
			}
			else {
				addSequential(new DriveProfile2("/home/lvuser/hopper4_blue.csv"));
				addSequential(new AutonStopMotors());
				addParallel(new AutonExtend());
				addSequential(new AutonDriveTurn(0.001, -40));
			}
			addSequential(new AutonStopMotors());
			addSequential(new AutonDriveTurn(0.8, 0));
			addSequential(new AutonStopMotors());
			addParallel(new AutonAlign(alliance == Alliance.Red));
			addSequential(new Shoot());
		default:
			break;
		}
	}
}