package com.frc1747;

import lib.frc1747.controller.Logitech;

public class OI {
	
	private Logitech driver;
	private Logitech operator;
	private static OI instance;
		
	private OI(){
		driver = new Logitech(RobotMap.DRIVER);
		operator = new Logitech(RobotMap.OPERATOR);
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
	
	public Logitech getOperator() {
		return operator;
	}
}
		

