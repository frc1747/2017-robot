package com.frc1747.commands.auton;

import java.util.logging.Level;

import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

import com.frc1747.Robot;
import com.frc1747.Robot.Autons;
import com.frc1747.commands.AutonAlign;
import com.frc1747.commands.Delay;
//import com.frc1747.commands.auton.BoilerHorizontalAutoAlign;
import com.frc1747.commands.collector.Extend;
import com.frc1747.commands.collector.Retract;
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
		
		auton = Autons.HOPPER;
		Alliance alliance = DriverStation.getInstance().getAlliance();
		
		/*switch(profile){
		case HOPPER:
			addParallel(new AutonExtend());
			//these two run in parallel
			if(alliance == Alliance.Red){
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper3a_red.csv"));
				addSequential(new Delay(1250));
				addParallel(new AutonBallClear());
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper3b_red.csv"));
				addSequential(new Rotate(42));
			}else{
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
		default:
			break;
		}*/
		
		addParallel(new AutonExtend());
		//addSequential(DriveProfile.fromFile("/home/lvuser/hopper4_blue.csv"));
		addSequential(new DriveProfile2("/home/lvuser/hopper4_blue.csv"));
		addParallel(new AutonStopMotors());
		addSequential(new Shoot());
	}	
}