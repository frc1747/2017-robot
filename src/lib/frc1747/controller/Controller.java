package lib.frc1747.controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public abstract class Controller {
	
	Joystick stick;

	JoystickButton xButton, aButton, bButton, yButton, 
	leftBumper, rightBumper, leftTrigger, rightTrigger;
	
	JoystickButton [] buttons = new JoystickButton [] { null, 
			xButton, aButton, bButton, yButton, 
			leftBumper, rightBumper, leftTrigger, rightTrigger
	};
	
	public static final int X = 1, A = 2, B = 3, Y = 4, LB = 5, 
			RB = 6, LT = 7, RT = 8;
	
	public static final int UP = 0, RIGHT = 90, DOWN = 180, LEFT = 270;
	public static final int UP_RIGHT = 45, DOWN_RIGHT = 135, DOWN_LEFT = 225, UP_LEFT = 315;
	
	public static final int LEFT_HORIZONTAL = 0, RIGHT_HORIZONTAL = 2, 
		LEFT_VERTICAL = 1, RIGHT_VERTICAL = 3;
	
	public static final double DEADZONE = 0.075;
	
	public Controller(int port) {
	
		stick = new Joystick(port);
		
		buttons[X] = new JoystickButton(stick, X);
		buttons[A] = new JoystickButton(stick, A);
		buttons[B] = new JoystickButton(stick, B);
		buttons[Y] = new JoystickButton(stick, Y);
		buttons[LB] = new JoystickButton(stick, LB);
		buttons[RB] = new JoystickButton(stick, RB);
	}
		
	public JoystickButton getButton(int buttonName) {
		return buttons[buttonName];
	}

	public double getAxis(int axisName) {
		
		double stickVal = stick.getRawAxis(axisName);
		
		if(stickVal < DEADZONE && stickVal > -DEADZONE) {
			stickVal = 0;
		}
		
		if(axisName == LEFT_VERTICAL || axisName == RIGHT_VERTICAL) {
			stickVal *= -1;
		}
		
		return stickVal;
	}
	
	public boolean getDPADButton(int Angle){
		
		return Angle == stick.getPOV();
	}

}
