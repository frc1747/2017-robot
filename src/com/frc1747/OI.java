
package com.frc1747;

import com.frc1747.commands.IntakeShoot;
import com.frc1747.commands.AutoDrive;
import com.frc1747.commands.BoilerHorizontal;
import com.frc1747.commands.auton.AutoShoot;
import com.frc1747.commands.auton.BackupBoilerVerticalAlign;
import com.frc1747.commands.auton.BoilerAlign;
//import com.frc1747.commands.auton.BoilerHorizontalAutoAlign;
import com.frc1747.commands.auton.BoilerVerticalAutoAlign;
import com.frc1747.commands.climb.Climb;
import com.frc1747.commands.climb.StopClimb;
import com.frc1747.commands.collector.Extend;
import com.frc1747.commands.collector.Retract;
import com.frc1747.commands.collector.TakeIn;
import com.frc1747.commands.collector.TakeOut;
import com.frc1747.commands.conveyer.ConveyIn;
import com.frc1747.commands.conveyer.ConveyOut;
import com.frc1747.commands.shifter.ShiftDown;
import com.frc1747.commands.shifter.ShiftUp;
import com.frc1747.commands.shooter.CloseGates;
import com.frc1747.commands.shooter.OpenGates;
import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.controller.Logitech;
import lib.frc1747.controller.Xbox;
import lib.frc1747.controller.button.POVButton;

public class OI {
	
	private static OI instance;
	
	private Logitech driver;
	private Logitech operator;
	
	private POVButton dPadUp;
	private POVButton dPadLeft;
	private POVButton dPadRight;
	private POVButton dPadDown;
		
	private OI() {		
		driver = new Logitech(RobotMap.DRIVER);
		operator = new Logitech(RobotMap.OPERATOR);
		
		createDriver();
		createOperator();
		createDashboard();
	}
	
	private void createDriver() {
		
		dPadUp = new POVButton(driver, Logitech.UP);
		dPadUp.whenPressed(new Climb());
		
		dPadLeft = new POVButton(driver, Logitech.LEFT);
		dPadLeft.whileHeld(new OpenGates());
		
		dPadRight = new POVButton(driver, Logitech.RIGHT);
		dPadRight.whileHeld(new CloseGates());
		
		dPadDown = new POVButton(driver, Logitech.DOWN);
		dPadDown.whenPressed(new StopClimb());
		
		driver.getButton(Logitech.Y).whileHeld(new ConveyIn());
		driver.getButton(Logitech.RT).whileHeld(new TakeIn());
		//driver.getButton(Logitech.B).whenPressed(new Boiler());
		driver.getButton(Logitech.B).whenPressed(new AutoDrive());
		driver.getButton(Logitech.X).whileHeld(new ConveyOut());
		driver.getButton(Logitech.BACK).whenPressed(new ShiftDown());
		driver.getButton(Logitech.START).whenPressed(new ShiftUp());
		driver.getButton(Logitech.RB).whileHeld(new Shoot());
	}
	
	private void createOperator() {
		operator.getButton(Logitech.LT).whileHeld(new Extend());
		operator.getButton(Logitech.LB).whileHeld(new Retract());
	}
	
	private void createDashboard() {
		SmartDashboard.putData("ShiftUp", new ShiftUp());
		SmartDashboard.putData("ShiftDown", new ShiftDown());
	}
	
	public static OI getInstance(){
		return instance == null ? instance = new OI() : instance;
	}
	
	public Logitech getDriver() {
		return driver;
	}
	
	public Logitech getOperator() {
		return operator;
	}
}