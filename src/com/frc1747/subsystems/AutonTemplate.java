package com.frc1747.subsystems;

import com.frc1747.commands.auton.AutoShoot;
import com.frc1747.commands.auton.BoilerAutoAlign;
import com.frc1747.commands.drive.DriveProfile;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTemplate extends CommandGroup{
	
	DriveProfile profile5;
	DriveProfile profile6;
	
	public AutonTemplate(int profile){
		
		DriveProfile profile6 = DriveProfile.fromFile("/home/lvuser/profile7a.csv");
		DriveProfile profile5 = DriveProfile.fromFile("/home/lvuser/profile7b.csv");
		
		this.profile5 = profile5;
		this.profile6 = profile6;
		
		switch(profile){
		case 5:
			System.out.println("Case 5");
//			addSequential(profile5);
//			addSequential(profile6);
			break;
		case 6:
			System.out.println("Case 6");
//			addSequential(profile6);
//			addSequential(profile5);
			break;
		}
	}	
}
