package com.frc1747.subsystems;

import com.frc1747.commands.auton.AutoShoot;
import com.frc1747.commands.auton.BoilerAutoAlign;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTemplate extends CommandGroup{
	
	
	AutonTemplate(String command1, String command2, String command3, String command4){
		
		String [] commands = {command1, command2, command3, command4}; //can add more commands
		
		for(String currentCommand : commands){
			switch(currentCommand){
			case "" : break;
			case "shoot": addShooter();
				break;
			case "boilerAlign": addBoilerAlignment();
				break;
			//add more cases for each possible command
			}
		}
	}
	
	private void addShooter(){
		addSequential(new AutoShoot());
	}
	
	private void addBoilerAlignment(){
		addSequential(new BoilerAutoAlign());
	}
	
}
