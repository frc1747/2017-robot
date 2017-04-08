package com.frc1747;

import com.frc1747.commands.auton.AutoAlign;
import com.frc1747.commands.climb.Climb;
import com.frc1747.commands.climb.ClimbPower;
import com.frc1747.commands.climb.StopClimb;
import com.frc1747.commands.collector.Extend;
import com.frc1747.commands.collector.HopperHoldForExtend;
import com.frc1747.commands.collector.Retract;
import com.frc1747.commands.conveyer.ConveyIn;
import com.frc1747.commands.conveyer.ConveyOut;
import com.frc1747.commands.gear.GearMechClose;
import com.frc1747.commands.gear.GearMechOpen;
import com.frc1747.commands.gear.GearToggle;
import com.frc1747.commands.shifter.ShiftDown;
import com.frc1747.commands.shifter.ShiftUp;
import com.frc1747.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.controller.Logitech;
import lib.frc1747.controller.Xbox;
import lib.frc1747.controller.button.POVButton;

public class OI {
	
	private static OI instance;
	
	private Logitech driver;
	private Xbox operator;
	
	private POVButton dPadUp;
	private POVButton dPadLeft;
	private POVButton dPadRight;
	private POVButton dPadDown;
	
	private POVButton operatorDPadUp, operatorDPadDown, operatorDPadLeft, operatorDPadRight;
	
	static final double CLIMBER_POWER = 1.0;
		
	private OI() {		
		driver = new Logitech(RobotMap.DRIVER);
		operator = new Xbox(RobotMap.OPERATOR);
		
		createDriver();
		createOperator();
		createDashboard();
	}
	
	private void createDriver() {
		
		dPadUp = new POVButton(driver, Logitech.UP);
		dPadUp.whenPressed(new ClimbPower(1));
		
		dPadLeft = new POVButton(driver, Logitech.LEFT);
		dPadLeft.whenPressed(new ClimbPower(0.2));
		
		dPadRight = new POVButton(driver, Logitech.RIGHT);
		dPadRight.whileHeld(new Climb());
		
		dPadDown = new POVButton(driver, Logitech.DOWN);
		dPadDown.whenPressed(new StopClimb());
		
		
		driver.getButton(Logitech.Y).whileHeld(new ConveyIn());
//		driver.getButton(Logitech.RT).whileHeld(new TakeIn());
		//driver.getButton(Logitech.B).whenPressed(new Boiler());
		driver.getButton(Logitech.B).whenPressed(new AutoAlign());
		driver.getButton(Logitech.X).whileHeld(new ConveyOut());
		driver.getButton(Logitech.BACK).whenPressed(new ShiftDown());
		driver.getButton(Logitech.START).whenPressed(new ShiftUp());
		driver.getButton(Logitech.RB).whileHeld(new Shoot());
		driver.getButton(Logitech.LB).whileHeld(new GearToggle());
	}
	
	private void createOperator() {
//		operator.getButton(Xbox.LT).whileHeld(new Extend());
//		operator.getButton(Xbox.LB).whileHeld(new Retract());
		//operator.getAxis()
		
		operatorDPadUp = new POVButton(operator, Xbox.UP);
		operatorDPadUp.whileHeld(new ShiftUp());
		
		operatorDPadDown = new POVButton(operator, Xbox.DOWN);
		operatorDPadDown.whileHeld(new ShiftDown());
		
		operator.getButton(Xbox.Y).whenPressed(new Extend());
		operator.getButton(Xbox.B).whenPressed(new Retract());
		operator.getButton(Xbox.RB).whileHeld(new GearToggle());
		operator.getButton(Xbox.X).whileActive(new ConveyOut());
		operator.getButton(Xbox.LB).whileHeld(new HopperHoldForExtend());
	}
	
	private void createDashboard() {
		SmartDashboard.putData("ShiftUp", new ShiftUp());
		SmartDashboard.putData("ShiftDown", new ShiftDown());
		
		SmartDashboard.putData("Gear Open", new GearMechOpen());
		SmartDashboard.putData("Gear Close", new GearMechClose());
	}
	
	public static OI getInstance(){
		return instance == null ? instance = new OI() : instance;
	}
	
	public Logitech getDriver() {
		return driver;
	}
	
	public Xbox getOperator() {
		return operator;
	}
}