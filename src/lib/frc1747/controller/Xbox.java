package lib.frc1747.controller;

import lib.frc1747.controller.button.JoystickButton;

public class Xbox extends Controller{
	
	public static final int A = 1, B = 2, X = 3, Y = 4, BACK = 7, START = 8;
	public static final int LT = 2, RT = 3;
	
	public Xbox(int port) {
		super(port);

		buttons[X] = new JoystickButton(getStick(), X);
		buttons[A] = new JoystickButton(getStick(), A);
		buttons[B] = new JoystickButton(getStick(), B);
		buttons[Y] = new JoystickButton(getStick(), Y);
		buttons[START] = new JoystickButton(getStick(), START);
		buttons[BACK] = new JoystickButton(getStick(), BACK);
		
	}
	
	public double getLTAngle(){
		return getStick().getRawAxis(LT);
	}
	
	public double getRTAngle(){
		return getStick().getRawAxis(RT);
	}

}
