package com.frc1747.subsystems;

import com.frc1747.Robot.Autons;
import com.frc1747.commands.auton.AutoShoot;
import com.frc1747.commands.auton.BoilerAutoAlign;
import com.frc1747.commands.drive.DriveProfile;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTemplate extends CommandGroup{
	
	DriveProfile profile5;
	DriveProfile profile6;
	
	public AutonTemplate(Autons profile){
		
		DriveProfile profile6 = DriveProfile.fromFile("/home/lvuser/profile7a.csv");
		DriveProfile profile5 = DriveProfile.fromFile("/home/lvuser/profile7b.csv");
		
		this.profile5 = profile5;
		this.profile6 = profile6;
		
		switch(profile){
		case GEAR_LEFT:
			//addSequential(DriveProfile.fromFile("/home/lvuser/gear_left_a.csv"));
			//addSequential(DriveProfile.fromFile("/home/lvuser/gear_left_b.csv"));
			break;
		case GEAR_RIGHT:
//			addSequential(DriveProfile.fromFile("/home/lvuser/gear_right_a.csv"));
//			addSequential(DriveProfile.fromFile("/home/lvuser/gear_right_a_b.csv"));
			break;
		case HOPPER_RED:
//			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_red_a.csv"));
//			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_red_b.csv"));
//			addSequential(new AutoShoot());
			break;
		case HOPPER_BLUE:
			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_blue_a.csv"));
			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_blue_b.csv"));
			addSequential(new AutoShoot());
			break;
		}
	}	
}
