package lib.frc1747.controller;

import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Xbox extends Controller{
	
	public static final int A = 1, B = 2, X = 3, Y = 4, BACK = 7, START = 8;
	public static final int LT = 4, RT = 5;
	
	

	public Xbox(int port) {
		super(port);

		buttons[X] = new JoystickButton(stick, X);
		buttons[A] = new JoystickButton(stick, A);
		buttons[B] = new JoystickButton(stick, B);
		buttons[Y] = new JoystickButton(stick, Y);
		buttons[START] = new JoystickButton(stick, START);
		buttons[BACK] = new JoystickButton(stick, BACK);
		
	}
	
	public double getLTAngle(){
		
		return stick.getRawAxis(LT);
	}
	
	public double getRTAngle(){
		
		return stick.getRawAxis(RT);
	}

}
