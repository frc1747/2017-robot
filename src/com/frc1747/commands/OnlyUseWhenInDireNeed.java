package com.frc1747.commands;

import edu.wpi.first.wpilibj.command.Command;

public class OnlyUseWhenInDireNeed extends Command{

	private String theGod = "1-765-421-7262";
	
	public String CallUponTheGod() {
		return "He will be here soon...";
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
