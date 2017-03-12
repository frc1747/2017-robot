package lib.frc1747.controller;

import lib.frc1747.controller.button.JoystickButton;

public class Logitech extends Controller{
	
	public static final int X = 1, A = 2, B = 3, Y = 4, LT = 7, RT = 8, BACK = 9, START = 10;
	public static final int RIGHT_HORIZONTAL = 2, RIGHT_VERTICAL = 3;

	public Logitech(int port){
		super(port);
		
		buttons[X] = new JoystickButton(getStick(), X);
		buttons[A] = new JoystickButton(getStick(), A);
		buttons[B] = new JoystickButton(getStick(), B);
		buttons[Y] = new JoystickButton(getStick(), Y);
		buttons[LT] = new JoystickButton(getStick(), LT);
		buttons[RT] = new JoystickButton(getStick(), RT);
		buttons[START] = new JoystickButton(getStick(), START);
		buttons[BACK] = new JoystickButton(getStick(), BACK);
	}
	
	public double getAxis(int axisName) {
		
		double stickVal = getStick().getRawAxis(axisName);
		
		if(stickVal < DEADZONE && stickVal > -DEADZONE) {
			stickVal = 0;
		}
		
		if(axisName == LEFT_VERTICAL || axisName == RIGHT_VERTICAL) {
			stickVal *= -1;
		}
		
		return stickVal;
	}
}
