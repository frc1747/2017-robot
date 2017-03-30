
package com.frc1747;

import com.frc1747.Robot.Autons;
import com.frc1747.commands.IntakeShoot;
import com.frc1747.commands.TeleopAlign;
import com.frc1747.commands.AutonAlign;
import com.frc1747.commands.BoilerHorizontal;
import com.frc1747.commands.auton.AutoShoot;
import com.frc1747.commands.auton.BackupBoilerVerticalAlign;
import com.frc1747.commands.auton.BoilerAlign;
//import com.frc1747.commands.auton.BoilerHorizontalAutoAlign;
import com.frc1747.commands.auton.BoilerVerticalAutoAlign;
import com.frc1747.commands.climb.Climb;
import com.frc1747.commands.climb.ClimbPower;
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
import lib.frc1747.commands.AutonChooser;
import lib.frc1747.commands.AutonNext;
import lib.frc1747.commands.AutonPrev;
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
	
	private POVButton operatorDPadUp;
	private POVButton operatorDPadDown;
	
	static final double CLIMBER_POWER = 1.0;
	
	private AutonChooser chooser;
		
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
		driver.getButton(Logitech.RT).whileHeld(new TakeIn());
		//driver.getButton(Logitech.B).whenPressed(new Boiler());
		driver.getButton(Logitech.B).whenPressed(new TeleopAlign());
		driver.getButton(Logitech.X).whileHeld(new ConveyOut());
		driver.getButton(Logitech.BACK).whenPressed(new ShiftDown());
		driver.getButton(Logitech.START).whenPressed(new ShiftUp());
		driver.getButton(Logitech.RB).whileHeld(new Shoot());
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
		operator.getButton(Xbox.A).whileHeld(new ConveyIn());
		operator.getButton(Xbox.X).whileActive(new ConveyOut());
		
		// Auton chooser stuff
		chooser = new AutonChooser(Autons.class);
		operatorDPadUp.whenPressed(new AutonNext(chooser));
		operatorDPadDown.whenPressed(new AutonPrev(chooser));
		chooser.start();
	}
	
	public Enum<?> getSelectedAuton() {
		return chooser.getSelectedAuton();
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
	
	public Xbox getOperator() {
		return operator;
	}
}