
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
	private Xbox operator;
	
	private POVButton dPadUp;
	private POVButton dPadLeft;
	private POVButton dPadRight;
	private POVButton dPadDown;
		
	private OI() {
		
		/** Please read this
		 *
		 *  whileHeld is not the correct implementation of having something
		 *  run while you are pressing a button. If you look at the documentation
		 *  for whileHeld(), it calls the command's start() method repeatedly. This
		 *  could explain some strange behavior.
		 *  
		 *  my suggestion: have a command that starts such a command using
		 *  .whenPressed(Command arg);
		 *  but once you are let go, do
		 *  .whenReleased(Command arg);
		 *  for the whenRelease command, you would make a new command that
		 *  cancels a certain command.
		 *  
		 *  It would be something like this:
		 *  .whenPressed(conveyIn = new ConveyIn());
		 *  .whenReleased(new CancelCommand(conveyIn));
		 *  
		 */
		
		driver = new Logitech(RobotMap.DRIVER);
		//operator = new Xbox(RobotMap.OPERATOR);
		
		createDriver();
		//createOperator();
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
		//driver.getButton(Logitech.A).whileHeld(new CloseGates());
		driver.getButton(Logitech.RT).whileHeld(new TakeIn());
		//driver.getButton(Logitech.B).whileHeld(new ConveyIn());
//		driver.getButton(Logitech.LT).whenPressed(new Extend());
//		driver.getButton(Logitech.LB).whenPressed(new Retract());
		//driver.getButton(Logitech.B).whenPressed(new Boiler());
		driver.getButton(Logitech.B).whenPressed(new AutoDrive());
		driver.getButton(Logitech.X).whileHeld(new ConveyOut());
		driver.getButton(Logitech.BACK).whenPressed(new ShiftDown());
		driver.getButton(Logitech.START).whenPressed(new ShiftUp());
		SmartDashboard.putData("ShiftUp", new ShiftUp());
		SmartDashboard.putData("ShiftDown", new ShiftDown());
		driver.getButton(Logitech.RB).whileHeld(new Shoot());
	}
	
	private void createOperator() {
		
		operator.getButton(Xbox.LT).whileHeld(new ConveyIn());
		operator.getButton(Xbox.RT).whileHeld(new ConveyOut());
		operator.getButton(Xbox.X).whileHeld(new IntakeShoot());
		////operator.getButton(Xbox.B).whileHeld(new ConveyIn());
		operator.getButton(Xbox.B).whenPressed(new Extend());
		operator.getButton(Xbox.Y).whenPressed(new Retract());
		operator.getButton(Xbox.LB).whileHeld(new TakeIn());
		operator.getButton(Xbox.RB).whileHeld(new TakeOut());
		operator.getButton(Xbox.A).whileHeld(new AutoShoot());
//		operator.getButton(Xbox.BACK).whenPressed(new ShiftDown());
//		operator.getButton(Xbox.START).whenPressed(new ShiftUp());	
	}
	
	private void createDashboard() {
		
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