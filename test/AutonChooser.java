

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.logging.Level;

import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;
import lib.frc1747.subsystems.HBRSubsystem;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonChooser<E extends Enum<E>>  {
	// Followers to use
	private E[] modes;
	private int n_modes;
	
    public AutonChooser() {
    	Logger logger = Instrumentation.getLogger("Auton Chooser");
    	
    	TypeVariable typeVariable = this.getClass().getTypeParameters()[0];
		//Class typeArguments = (Class)typeVariable;
		//typeVariable.
		//ogger.log(Level.INFO, typeVariable.toString());
		
    	//ParameterizedType type = (ParameterizedType)(this.getClass());
    	//type.
    	
		// Get the enum containing the systems to control (followers to use)
    	/*
		Class<?> type = this.getClass();
		while(!type.getSuperclass().getName().equals(HBRSubsystem.class.getName())) {
			type = type.getSuperclass();
		}
		if(type.getGenericSuperclass() instanceof ParameterizedType) {
			ParameterizedType superType = (ParameterizedType)type.getGenericSuperclass();
			Class<?> typeArguments = (Class<?>)superType.getActualTypeArguments()[0];
			followers = (E[])typeArguments.getEnumConstants();
			n_followers = followers.length;
		}
		*/
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
}
