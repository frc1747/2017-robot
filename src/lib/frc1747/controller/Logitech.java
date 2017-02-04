package lib.frc1747.controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Logitech extends Controller{
	
	public static final int X = 1, A = 2, B = 3, Y = 4, BACK = 9, START = 10;

	public Logitech(int port){
		super(port);
		
		buttons[X] = new JoystickButton(stick, X);
		buttons[A] = new JoystickButton(stick, A);
		buttons[B] = new JoystickButton(stick, B);
		buttons[Y] = new JoystickButton(stick, Y);
		buttons[LT] = new JoystickButton(stick, LT);
		buttons[RT] = new JoystickButton(stick, RT);
//		buttons[START] = new JoystickButton(stick, START);
//		buttons[BACK] = new JoystickButton(stick, BACK);
	}
}
