package lib.frc1747.controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Logitech extends Controller{

	public Logitech(int port){
		super(port);
		
		buttons[LT] = new JoystickButton(stick, LT);
		buttons[RT] = new JoystickButton(stick, RT);
	}
}
