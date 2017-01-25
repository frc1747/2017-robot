package com.frc1747;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class LogitechController {


	Joystick stick;
	JoystickButton xButton, aButton, bButton, yButton, leftBumper, rightBumper, leftTrigger, rightTrigger;
	
	

	public LogitechController(int port){
		stick = new Joystick(port);
		xButton = new JoystickButton(stick, 1);
		aButton = new JoystickButton(stick, 2);
		bButton = new JoystickButton(stick, 3);
		yButton = new JoystickButton(stick, 4);
		leftBumper = new JoystickButton(stick, 5);
		rightBumper = new JoystickButton(stick, 6);
		leftTrigger = new JoystickButton(stick, 7);
		rightTrigger = new JoystickButton(stick, 8);
		//// CREATING BUTTONS
		// One type of button is a joystick button which is any button on a
		//// joystick.
		// You create one by telling it which joystick it's on and which button
		// number it is.
		// Joystick stick = new Joystick(port);
		// Button button = new JoystickButton(stick, buttonNumber);
		
		

		// There are a few additional built in buttons you can use. Additionally,
		// by subclassing Button you can create custom triggers and bind those to
		// commands the same as any other Button.

		//// TRIGGERING COMMANDS WITH BUTTONS
		// Once you have a button, it's trivial to bind it to a button in one of
		// three ways:

		// Start the command when the button is pressed and let it run the command
		// until it is finished as determined by it's isFinished method.
		// button.whenPressed(new ExampleCommand());

		// Run the command while the button is being held down and interrupt it once
		// the button is released.
		// button.whileHeld(new ExampleCommand());

		// Start the command when the button is released and let it run the command
		// until it is finished as determined by it's isFinished method.
		// button.whenReleased(new ExampleCommand());
		}
		
	public Button getButtonX(){
		return xButton;
	}
	public Button getButtonA(){
		return aButton;
	}
	public Button getButtonB(){
		return bButton;
	}
	public Button getButtonY(){
		return yButton;
	}
	public Button getButtonLB(){
		return leftBumper;
	}
	public Button getButtonRB(){
		return rightBumper;
	}
	public Button getButtonLT(){
		return leftTrigger;
	}
	public Button getButtonRT(){
		return rightTrigger;
	}
	public double getleftHorizontal(){
		
		return stick.getRawAxis(0);
		
	}
	
	public int getDpadAngle(){
		return stick.getPOV();
	}
	public double getLeftVertical(){
		double value;
		if (0.075 > stick.getRawAxis(1) && -0.075 < stick.getRawAxis(1)){
			value = 0;
		}else{
			value = -stick.getRawAxis(1);
		}
		
		return value;
	}
	
	public double getRightHorizontal(){
		double value;
		if (0.075 > stick.getRawAxis(2) && -0.075 < stick.getRawAxis(2)){
			value = 0;
		}else{
			value = stick.getRawAxis(2);
		}
		return value;
	}
}
