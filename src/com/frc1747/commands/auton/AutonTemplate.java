package com.frc1747.commands.auton;

import com.frc1747.Robot;
import com.frc1747.Robot.Autons;
import com.frc1747.commands.AutonAlign;
import com.frc1747.commands.Delay;
//import com.frc1747.commands.auton.BoilerHorizontalAutoAlign;
import com.frc1747.commands.collector.Extend;
import com.frc1747.commands.collector.Retract;
import com.frc1747.commands.drive.DriveProfile;
import com.frc1747.commands.drive.Rotate;
import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTemplate extends CommandGroup{
	public AutonTemplate(Autons profile){
		profile = Autons.HOPPER;
		Alliance alliance = DriverStation.getInstance().getAlliance();
		
		switch(profile){
		case HOPPER:
			if(alliance == Alliance.Red){
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper2_red.csv"));
			}else{
				addSequential(DriveProfile.fromFile("/home/lvuser/hopper2_blue.csv"));
			}
			addSequential(new AutonExtend());
			addSequential(new AutonAlign(alliance == Alliance.Red));
			addSequential(new Shoot());
			break;
		default:
			break;
		}
	}	
}