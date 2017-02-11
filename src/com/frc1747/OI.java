package com.frc1747;

import com.frc1747.commands.Increment;
import com.frc1747.commands.MotorTest;
import com.frc1747.commands.collector.Extend;
import com.frc1747.commands.collector.Retract;
import com.frc1747.commands.collector.TakeIn;
import com.frc1747.commands.collector.TakeOut;
import com.frc1747.commands.conveyer.ConveyIn;
import com.frc1747.commands.conveyer.ConveyOut;
import com.frc1747.commands.drive.ResetGyro;
import com.frc1747.commands.shifter.ShiftDown;
import com.frc1747.commands.shifter.ShiftUp;
import com.frc1747.commands.shooter.Shoot;

import lib.frc1747.controller.Logitech;
import lib.frc1747.controller.POVButton;
import lib.frc1747.controller.Xbox;

public class OI {
	
	private Logitech driver;
	private Xbox operator;
	private static OI instance;
	private POVButton dPad;
		
	private OI(){
		System.out.println("OI Create");
		driver = new Logitech(RobotMap.DRIVER);
		operator = new Xbox(RobotMap.OPERATOR);
		
		dPad = new POVButton(driver, Logitech.UP);
		dPad.whenPressed(new Increment());

		driver.getButton(Logitech.LT).whileHeld(new ConveyIn());
		driver.getButton(Logitech.RT).whileHeld(new ConveyOut());
		driver.getButton(Logitech.X).whileHeld(new Shoot());
		//driver.getButton(Logitech.B).whileHeld(new ConveyIn());
		driver.getButton(Logitech.B).whenPressed(new Extend());
		driver.getButton(Logitech.Y).whenPressed(new Retract());
		driver.getButton(Logitech.LB).whileHeld(new TakeIn());
		driver.getButton(Logitech.RB).whileHeld(new TakeOut());
		driver.getButton(Logitech.BACK).whenPressed(new ShiftDown());
		driver.getButton(Logitech.START).whenPressed(new ShiftUp());
		
		operator.getButton(Xbox.LT).whileHeld(new ConveyIn());
		operator.getButton(Xbox.RT).whileHeld(new ConveyOut());
		operator.getButton(Xbox.X).whileHeld(new Shoot());
		////operator.getButton(Xbox.B).whileHeld(new ConveyIn());
		operator.getButton(Xbox.B).whenPressed(new Extend());
		operator.getButton(Xbox.Y).whenPressed(new Retract());
		operator.getButton(Xbox.LB).whileHeld(new TakeIn());
		operator.getButton(Xbox.RB).whileHeld(new TakeOut());
//		operator.getButton(Xbox.BACK).whenPressed(new ShiftDown());
//		operator.getButton(Xbox.START).whenPressed(new ShiftUp());
		
		
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
		

