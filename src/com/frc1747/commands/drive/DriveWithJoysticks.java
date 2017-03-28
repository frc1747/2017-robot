package com.frc1747.commands.drive;

import com.frc1747.OI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import lib.frc1747.controller.Logitech;
import lib.frc1747.controller.Xbox;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoysticks extends Command {
	private DriveSubsystem drive;

	// Timing in the motion profile
	private Timer timer;

	// Maximum robot parameters
	private double s_v_max = 12;
	private double a_v_max = 4;
	
	// Feedforward constants
	private double s_kf_v = 1 / s_v_max;
	private double a_kf_v = 1 / 6.71;

	// Feedback constants
	private double s_kp = 0.01;
	private double s_ki = 0;
	private double s_kd = 0;
	
	private double a_kp = 0.4;
	private double a_ki = 0;
	private double a_kd = 0;

	// Clamping variables
	private double s_lim_p = 1;
	private double s_lim_i = 1;
	private double s_lim_d = 1;
	private double s_lim_q = 1;
	
	private double a_lim_p = 1;
	private double a_lim_i = 1;
	private double a_lim_d = 1;
	private double a_lim_q = 1;

	private double lim_q = 1;

	// Sensor inputs
	private double s_v;
	private double a_v;

	// Error variables
	private double s_ep;
	private double s_ei;
	private double s_ed;
	
	private double a_ep;
	private double a_ei;
	private double a_ed;
	
	private double deadband = 0.05;
	private double offset = 0.0;
	
	// Logging
	private File file;
	private PrintWriter print;
	
	double dt = 0.02;

    public DriveWithJoysticks() {
    	// Command initialization
    	requires(drive = DriveSubsystem.getInstance());
    	setInterruptible(true);

		// Ensure the timer is initialized
		timer = null;
		
		// Initialize logging
		try{
			file = File.createTempFile("log_drive_", ".csv", new File("/home/lvuser"));
			print = new PrintWriter(new FileOutputStream(file.getAbsolutePath()));
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//System.out.println("DRIVE INIT");
    	if (isFinished()) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new CalculateClass(), 0,
					(long) (dt * 1000));

			// Reset sensors
	    	//drive.resetEncoders();
	    	//drive.resetGyro();

			// Zero sensor inputs
			s_v = 0;
			a_v = 0;

			// Zero error variables
			s_ep = 0;
			s_ei = 0;
			s_ed = 0;
			
			a_ep = 0;
			a_ei = 0;
			a_ed = 0;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {}

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//System.out.println("DRIVE FINISHED?");
        return timer == null;
    }

    // May be called multiple times in this class
    protected void end() {
    	//System.out.println("DRIVE END");
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
    		//System.out.println("DRIVE_LOOP");
    		// ----------------------------------------
    		// Calculate for translational
    		
			// Follower variables
			double s_p_v = s_v_max * Math.pow(OI.getInstance().getDriver().getAxis(Logitech.LEFT_VERTICAL), 1.0);
			if(OI.getInstance().getDriver().getButton(Logitech.LT).get()) {
				s_p_v *= 0.3;
			}
			s_p_v += s_v_max * 0.2 * Math.pow(OI.getInstance().getOperator().getAxis(Xbox.LEFT_VERTICAL), 1.0);

			// Read the velocity
			double s_m_v = drive.getAverageSpeed();//intentionally out of phase
			////System.out.println("                             " + s_p_v + " , " + s_m_v);
			//System.out.println("                                 " + s_m_v);
			//System.out.println("                                 " + drive.getLeftSpeed() + ", " + drive.getRightSpeed());
			// Proportional error
			s_ep = s_p_v - s_m_v;
			// Integral error
			s_ei += s_ep * dt;
			// Derivative error
			s_ed = (s_v - s_m_v) / dt;

			// Update the sensor variable
			s_v = s_m_v;
			
			// Calculate the Output Value
			double s_output = 0;
			s_output += s_kf_v * s_p_v;
			s_output += Math.min(s_lim_p, Math.max(-s_lim_p, s_kp * s_ep));
			s_output += Math.min(s_lim_i, Math.max(-s_lim_i, s_ki * s_ei));
			s_output += Math.min(s_lim_d, Math.max(-s_lim_d, s_kd * s_ed));

			// Limit and scale the output
			s_output = Math.min(1, Math.max(-1, s_output)) * s_lim_q;

    		// ----------------------------------------			
			// Calculate for rotational
    		
			// Follower variables
			double a_p_v = -a_v_max * Math.pow(OI.getInstance().getDriver().getAxis(Logitech.RIGHT_HORIZONTAL), 1.0);
			if(OI.getInstance().getDriver().getButton(Logitech.LT).get()) {
				a_p_v *= 0.3;
			}
			
			a_p_v += a_v_max * 0.2 * Math.pow(-OI.getInstance().getOperator().getAxis(Xbox.RIGHT_HORIZONTAL), 1.0);
			
			// Read the heading
			double a_m_v = -drive.getGyro().getRate();

			// Position error
			a_ep = a_p_v - a_m_v;
			// Integral error
			a_ei += a_ep * dt;
			// Derivative error
			a_ed = (a_v - a_m_v) / dt;

			// Update the sensor variable
			a_v = a_m_v;
			
			// Calculate the Output Value
			double a_output = 0;
			a_output += a_kf_v * a_p_v;
			a_output += Math.min(a_lim_p, Math.max(-a_lim_p, a_kp * a_ep));
			a_output += Math.min(a_lim_i, Math.max(-a_lim_i, a_ki * a_ei));
			a_output += Math.min(a_lim_d, Math.max(-a_lim_d, a_kd * a_ed));
			if(Math.abs(a_p_v) > deadband){
				a_output += offset * Math.signum(a_p_v);
			}

			// Limit and scale the output
			a_output = Math.min(1, Math.max(-1, a_output)) * a_lim_q;

			// Combine the outputs
			double outputL = Math.min(lim_q, Math.max(-lim_q, s_output - a_output));
			double outputR = Math.min(lim_q, Math.max(-lim_q, s_output + a_output));
			drive.setPower(outputL, outputR);

			// Logging
			if(print != null) {
				//System.out.println("LOG");
				print.format("%.4f, %.4f, %.4f, %.4f\n", s_p_v, s_m_v, a_p_v, a_m_v);
			}
    	}
    }
}