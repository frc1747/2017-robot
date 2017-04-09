package com.frc1747.commands.auton;

import java.util.logging.Level;

import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

import com.frc1747.Robot.Autons;
import com.frc1747.commands.Delay;
import com.frc1747.commands.drive.DriveProfile;
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
			addSequential(new AutonStopMotors());
			if(alliance == Alliance.Red){
				addSequential(new DriveProfile("/home/lvuser/hopper3a_red.csv"));
				addSequential(new AutonStopMotors());
				addSequential(new Delay(1500));
				addSequential(new AutonDriveTurn(-1.5, -70));
			}
			else {
				addSequential(new DriveProfile("/home/lvuser/hopper3a_blue.csv"));
				addSequential(new AutonStopMotors());
				addSequential(new Delay(1500));
				addSequential(new AutonDriveTurn(-1.5, 70));
			}
			addParallel(new AutonStopMotors());
			addParallel(new Shoot());
			addSequential(new Delay(750));
			addSequential(new AutoAlign());
//			addSequential(new Delay(200));
//			addSequential(new AutoAlign());
			addSequential(new AutonStopMotors());
			break;
		case HOPPER2:
			addSequential(new AutonStopMotors());
			if(alliance == Alliance.Red) {
				addSequential(new DriveProfile("/home/lvuser/hopper4_red.csv"));
				addSequential(new AutonStopMotors());
				addParallel(new AutonExtend());
				addSequential(new AutonDriveTurn(0.001, 40));
			}
			else {
				addSequential(new DriveProfile("/home/lvuser/hopper4_blue.csv"));
				addSequential(new AutonStopMotors());
				addParallel(new AutonExtend());
				addSequential(new AutonDriveTurn(0.001, -40));
			}
			addSequential(new AutonStopMotors());
			addSequential(new AutonDriveTurn(0.8, 0));
			addSequential(new AutonStopMotors());
			addParallel(new AutoAlign());
			addSequential(new Shoot());
			break;
		case JUST_SHOOT:
			addParallel(new AutonExtend());
			addSequential(new Shoot());
			break;
		case CENTER_GEAR:
			addSequential(new AutonExtend());
			addParallel(new AutonRetract());
			addSequential(new AutonStopMotors());
			addSequential(new AutonDriveTurnSlow(4.4, 0));
			addParallel(new AutonStopMotors());
			addParallel(new AutonGearRelease());
			addSequential(new Delay(500));
			addSequential(new AutonStopMotors());
			addSequential(new AutonDriveTurnSlow(-2, 0));
			addSequential(new AutonStopMotors());
			addSequential(new AutonDriveTurn(0, alliance == Alliance.Red ? -105 : 105));
			addSequential(new AutonStopMotors());
			addSequential(new AutonDriveTurn(6.5, 0));
			addSequential(new AutonStopMotors());
			addSequential(new Delay(250));
			addSequential(new AutonStopMotors());
			addParallel(new AutoAlign());
			addSequential(new Shoot());
			break;
		case TEST_AUTON:
			addSequential(new AutonStopMotors());
			addSequential(new AutonDriveTurn(-1.5, -45));
			addSequential(new AutonStopMotors());
			break;
		case TEST_AUTON_2:
			addSequential(new AutonStopMotors());
			addSequential(new AutoAlign());
			addSequential(new AutonStopMotors());
			break;
		default:
			break;
		}
	}
}