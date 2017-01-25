package com.frc1747;

import lib.frc1747.controller.Logitech;

public class OI {
	
	private Logitech driver;
	private Logitech operator;
		
	public OI(){
		driver = new Logitech(RobotMap.DRIVER);
		operator = new Logitech(RobotMap.OPERATOR);
	}
	
	public Logitech getDriver() {
		return driver;
	}
	
	public Logitech getOperator() {
		return operator;
	}
}
		

