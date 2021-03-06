import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

/**
 * Implements a simple joystick based method for choosing autons.
 * Also look at the AutonNext and AutonPrev commands.
 * @author Tiger Huang
 *
 */
public class AutonChooser {
	// Possible modes
	private Enum<?>[] modes;
	
	// Logger
	Logger logger;
	
	// Selected auton index;
	int index;
	
    public AutonChooser(Class<? extends Enum<?>> modesClass) {
    	logger = Instrumentation.getLogger("Auton Chooser");
    	logger.registerString("Selected Auton", true, false);
    	
    	// Get the modes
    	modes = modesClass.getEnumConstants();
    	
    	// Default
    	index = 0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	logger.putString("Selected Auton", modes[index].toString());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    public void nextAuton() {
    	index++;
    	if(index >= modes.length) index -= modes.length;
    }
    
    public void prevAuton() {
    	index--;
    	if(index < 0) index += modes.length;
    }
}
