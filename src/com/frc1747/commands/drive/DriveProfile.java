package com.frc1747.commands.drive;

import java.util.Timer;
import java.util.TimerTask;

import com.frc1747.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class DriveProfile extends Command {
	private DriveSubsystem drive;

	// Current profile point being used
	private int index;

	// Timing in the motion profile
	private Timer timer;

	// Motion profile parameters
	private double dt;
	private double[][] s_profile;
	private double[][] a_profile;

	// Feedforward constants
	private double s_kf_p;
	private double s_kf_v;
	private double s_kf_a;
	
	private double a_kf_p;
	private double a_kf_v;
	private double a_kf_a;

	// Feedback constants
	private double s_kp;
	private double s_ki;
	private double s_kd;
	
	private double a_kp;
	private double a_ki;
	private double a_kd;

	// Clamping variables
	private double s_lim_p;
	private double s_lim_i;
	private double s_lim_d;
	private double s_lim_q;
	
	private double a_lim_p;
	private double a_lim_i;
	private double a_lim_d;
	private double a_lim_q;

	private double lim_q;

	// Sensor inputs
	private double s_p;
	private double a_p;

	// Error variables
	private double s_ep;
	private double s_ei;
	private double s_ed;
	
	private double a_ep;
	private double a_ei;
	private double a_ed;

    public DriveProfile(double[][] profileS, double[][] profileA) {
    	// Command initialization
    	requires(drive = DriveSubsystem.getInstance());
    	setInterruptible(true);

		// Ensure the timer is initialized
		timer = null;
		
		// Set the time period
		dt = 0.01;
		
		// Set the profiles
		s_profile = profileS;
		a_profile = profileA;

		// Set feedforward constants
		s_kf_p = 0;
		s_kf_v = 0.1;
		s_kf_a = 0;
		
		a_kf_p = 0;
		a_kf_v = 0;
		a_kf_a = 0;

		// Set feedback constants
		s_kp = 0;
		s_ki = 0;
		s_kd = 0;
		
		a_kp = 0;
		a_ki = 0;
		a_kd = 0;

		// Ensure the clamping variables are initialized
		s_lim_p = 1;
		s_lim_i = 1;
		s_lim_d = 1;

		a_lim_p = 1;
		a_lim_i = 1;
		a_lim_d = 1;

		s_lim_q = 1;
		a_lim_q = 1;
		lim_q = 1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("PROFILE INIT");
    	if (isFinished()) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new CalculateClass(), 0,
					(long) (dt * 1000));

			// Reset sensors
	    	drive.disablePID();
	    	drive.resetEncoders();
	    	drive.resetGyro();

			// Zero sensor inputs
			s_p = 0;
			a_p = 0;

			// Zero error variables
			s_ep = 0;
			s_ei = 0;
			s_ed = 0;
			
			a_ep = 0;
			a_ei = 0;
			a_ed = 0;
			
			// Start at the beginning on the profile
			index = 0;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {}

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	System.out.println("PROFILE FINISHED?");
        return timer == null;
    }

    // May be called multiple times in this class
    protected void end() {
    	System.out.println("PROFILE END");
		if (!isFinished()) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
    	drive.setPower(0, 0);
    }

    protected void interrupted() {
    	end();
    }
    
    private class CalculateClass extends TimerTask {
    	// Main calculation loop
    	@Override
    	public void run() {
    		System.out.println("PROFILE_LOOP");
    		// ----------------------------------------
    		// Calculate for translational
    		
			// Profile variables
			double s_p_p = s_profile[index][2];
			double s_p_v = s_profile[index][1];
			double s_p_a = s_profile[index][0];

			// Read the position
			double s_m_p = drive.getAveragePosition();

			// Position error
			s_ep = s_p_p - s_m_p;
			// Integral error
			s_ei += s_ep * dt;
			// Derivative error
			s_ed = (s_p - s_m_p) / dt;

			// Update the sensor variable
			s_p = s_m_p;
			
			// Calculate the Output Value
			double s_output = 0;
			s_output += s_kf_p * s_p_p;
			s_output += s_kf_v * s_p_v;
			s_output += s_kf_a * s_p_a;
			s_output += Math.min(s_lim_p, Math.max(-s_lim_p, s_kp * s_ep));
			s_output += Math.min(s_lim_i, Math.max(-s_lim_i, s_ki * s_ei));
			s_output += Math.min(s_lim_d, Math.max(-s_lim_d, s_kd * s_ed));

			// Limit and scale the output
			s_output = Math.min(1, Math.max(-1, s_output)) * s_lim_q;

    		// ----------------------------------------			
			// Calculate for rotational
    		
			// Profile variables
			double a_p_p = a_profile[index][2];
			double a_p_v = a_profile[index][1];
			double a_p_a = a_profile[index][0];
			
			// Read the heading
			double a_m_p = -drive.getAngle();
			
			// Position error
			a_ep = a_p_p - a_m_p;
			// Integral error
			a_ei += a_ep * dt;
			// Derivative error
			a_ed = (a_p - a_m_p) / dt;

			// Update the sensor variable
			a_p = a_m_p;
			
			// Calculate the Output Value
			double a_output = 0;
			a_output += a_kf_p * a_p_p;
			a_output += a_kf_v * a_p_v;
			a_output += a_kf_a * a_p_a;
			a_output += Math.min(a_lim_p, Math.max(-a_lim_p, a_kp * a_ep));
			a_output += Math.min(a_lim_i, Math.max(-a_lim_i, a_ki * a_ei));
			a_output += Math.min(a_lim_d, Math.max(-a_lim_d, a_kd * a_ed));

			// Limit and scale the output
			a_output = Math.min(1, Math.max(-1, a_output)) * a_lim_q;

			// Combine the outputs
			double outputL = Math.min(lim_q, Math.max(-lim_q, s_output - a_output));
			double outputR = Math.min(lim_q, Math.max(-lim_q, s_output + a_output));
			drive.setPower(outputL, outputR);
			
			// Check end conditions
			index++;
			if (index >= s_profile.length) {
				end();
			}
    	}
    }
}