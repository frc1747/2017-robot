package com.frc1747;

import com.frc1747.commands.Increment;
import com.frc1747.commands.ResetGyro;

import lib.frc1747.controller.Controller;
import lib.frc1747.controller.Logitech;
import lib.frc1747.controller.POVButton;
import lib.frc1747.controller.Xbox;

public class OI {
	
	private Logitech driver;
	private Xbox operator;
	private static OI instance;
	private POVButton dPad;
		
	private OI(){
		driver = new Logitech(RobotMap.DRIVER);
		operator = new Xbox(RobotMap.OPERATOR);
		
		dPad = new POVButton(driver, Logitech.UP);
		dPad.whenPressed(new Increment());

		driver.getButton(Logitech.A).whileHeld(new ResetGyro());

	}
	
	public static OI getInstance(){
		if (instance == null){
			instance = new OI();
			
		}
		return instance;
	}
	
	public Logitech getDriver() {
		return driver;
	}
	
	public Xbox getOperator() {
		return operator;
	}
}
		

