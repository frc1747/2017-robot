package lib.frc1747.controller.button;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import lib.frc1747.controller.Controller;

public class POVButton extends JoystickButton {
	
	Controller joystick;
	int buttonAngle;
	
	public POVButton(Controller joystick, int buttonAngle) {
		super(joystick.getStick(), buttonAngle);
		
		this.joystick = joystick;
		this.buttonAngle = buttonAngle;
	}

	public boolean get(){
		return joystick.getDPADButton(buttonAngle);
	}
}
