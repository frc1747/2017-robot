package lib.frc1747.commands;

import java.util.logging.Level;

import edu.wpi.first.wpilibj.command.Command;
import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

/**
 *
 */
public class AutonNext extends Command {
	private AutonChooser chooser;
	private Logger logger;

    public AutonNext(AutonChooser chooser) {
    	logger = Instrumentation.getLogger("AutonNext");
    	this.chooser = chooser;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	logger.log(Level.INFO, "***NEXT AUTON***");
    	chooser.nextAuton();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	logger.log(Level.INFO, "***NEXT AUTON***");
    	System.out.println("**********next auton***********");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
