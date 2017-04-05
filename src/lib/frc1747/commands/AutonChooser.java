package lib.frc1747.commands;

import java.util.logging.Level;

import com.frc1747.commands.auton.AutonTemplate;

import edu.wpi.first.wpilibj.command.Command;
import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

/**
 * Implements a simple joystick based method for choosing autons.
 * Also look at the AutonNext and AutonPrev commands.
 * @author Tiger Huang
 *
 */
public class AutonChooser extends Command {
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
    	logger.log(Level.INFO, modes[index].toString());
    	logger.putString("Selected Auton", modes[index].toString());
    	System.out.println("***********Auton*************");
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
    	logger.log(Level.INFO, "***NEXT AUTON***");
    	index++;
    	if(index >= modes.length) index -= modes.length;
    }
    
    public void prevAuton() {
    	logger.log(Level.INFO, "***PREV AUTON***");
    	index--;
    	if(index < 0) index += modes.length;
    }
    
    public Enum<?> getSelectedAuton() {
    	return modes[index];
    }
    
    public String getAutonString(){
    	return modes[index].toString();
    }
}
