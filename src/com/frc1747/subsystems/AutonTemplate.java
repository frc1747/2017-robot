package com.frc1747.subsystems;

import com.frc1747.Robot;
import com.frc1747.Robot.Autons;
import com.frc1747.commands.auton.AutoShoot;
import com.frc1747.commands.auton.BoilerHorizontalAutoAlign;
import com.frc1747.commands.drive.DriveProfile;
import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTemplate extends CommandGroup{
	public AutonTemplate(Autons profile){
		profile = Robot.Autons.HOPPER_BLUE;
		
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
			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_red_a.csv"));
			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_red_b.csv"));
//			addSequential(new Shoot());
			break;
		case HOPPER_BLUE:
			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_blue_a.csv"));
			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_blue_b.csv"));
//			addSequential(new Shoot());
			break;
		}
	}	
}
