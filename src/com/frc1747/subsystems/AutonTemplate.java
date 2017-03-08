package com.frc1747.subsystems;

import com.frc1747.Robot;
import com.frc1747.Robot.Autons;
import com.frc1747.commands.Delay;
import com.frc1747.commands.auton.AutoShoot;
import com.frc1747.commands.auton.AutonExtend;
import com.frc1747.commands.auton.AutonRetract;
//import com.frc1747.commands.auton.BoilerHorizontalAutoAlign;
import com.frc1747.commands.collector.Extend;
import com.frc1747.commands.collector.Retract;
import com.frc1747.commands.drive.DriveProfile;
import com.frc1747.commands.drive.Rotate;
import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTemplate extends CommandGroup{
	public AutonTemplate(Autons profile){
		profile = Robot.Autons.HOPPER_RED;
		
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
			addSequential(DriveProfile.fromFile("/home/lvuser/hopper2_blue.csv"));
			addSequential(new Rotate(-10));
			addSequential(new Rotate(13));
//			addSequential(new AutonExtend());
//			addSequential(new AutonRetract());
			break;
		case HOPPER_BLUE:
//			addSequential(new Extend());
//			addSequential(new Delay());
//			addSequential(new Retract());
//			addSequential(new Delay());
//			addSequential(new AutoShoot());
//			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_blue_a.csv"));
//			addSequential(DriveProfile.fromFile("/home/lvuser/hopper_blue_b.csv"));
//			addSequential(DriveProfile.fromFile("/home/lvuser/10pt_blue.csv"));
			addSequential(new AutonExtend());
			addSequential(new AutonRetract());
			addSequential(DriveProfile.fromFile("/home/lvuser/5ft_turn.csv"));
			break;
		}
	}	
}
