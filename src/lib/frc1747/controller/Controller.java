package lib.frc1747.controller;

import edu.wpi.first.wpilibj.Joystick;
import lib.frc1747.controller.button.JoystickButton;

public abstract class Controller {
	
	private Joystick stick;

	JoystickButton xButton, aButton, bButton, yButton, 
	leftBumper, rightBumper, leftTrigger, rightTrigger, start, back;
	
	JoystickButton [] buttons = new JoystickButton [] { null, 
			xButton, aButton, bButton, yButton, 
			leftBumper, rightBumper, leftTrigger, rightTrigger, start, back
	};
	
	public static final int LB = 5, RB = 6;
	
	public static final int UP = 0, RIGHT = 90, DOWN = 180, LEFT = 270;
	public static final int UP_RIGHT = 45, DOWN_RIGHT = 135, DOWN_LEFT = 225, UP_LEFT = 315;
	
	public static final int LEFT_HORIZONTAL = 0, /*RIGHT_HORIZONTAL = 2,*/ 
		LEFT_VERTICAL = 1; /*RIGHT_VERTICAL = 3;*/
	
	public static final double DEADZONE = 0.075;
	
	public Controller(int port) {
	
		stick = new Joystick(port);
		
		buttons[LB] = new JoystickButton(getStick(), LB);
		buttons[RB] = new JoystickButton(getStick(), RB);
	}
		
	public JoystickButton getButton(int buttonName) {
		return buttons[buttonName];
	}
	
	public boolean getDPADButton(int Angle){
		
		return Angle == getStick().getPOV();
	}

	public Joystick getStick() {
		return stick;
	}
}
