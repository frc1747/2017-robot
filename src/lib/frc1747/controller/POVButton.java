package lib.frc1747.controller;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class POVButton extends JoystickButton{
	
	Controller joystick;
	int buttonAngle;
	
	public POVButton(Controller joystick, int buttonAngle) {
		super(joystick.stick, buttonAngle);
		this.joystick = joystick;
		this.buttonAngle = buttonAngle;
		// TODO Auto-generated constructor stub
	}

	public boolean get(){
		return joystick.getDPADButton(buttonAngle);
	}

}
