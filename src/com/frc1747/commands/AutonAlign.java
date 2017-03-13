package com.frc1747.commands;

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

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonAlign extends Command {
	private DriveSubsystem drive;

	// Timing in the motion profile
	private Timer timer;
	long startTime;

	// Feedback constants
	private double s_kp = 0.2;
	private double s_ki = .01/.05;
	private double s_kd = 0;
	
	private double a_kp = 0.04;
	private double a_ki = .01/.05;
	private double a_kd = 0.1*0.05;
	
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
	private double s_p;
	private double a_p;

	// Error variables
	private double s_ep;
	private double s_ei;
	private double s_ed;
	
	private double a_ep;
	private double a_ei;
	private double a_ed;
	
	private double deadband = 0.01;
	private double offset = 0.3;
	
	//Setpoint
	private double s_p_p;
	private double a_p_p;
	
	// Logging
	private File file;
	private PrintWriter print;
	
	private double dt = 0.02;
	
	// Red is true, blue is false
	private boolean alliance;

    public AutonAlign(boolean alliance) {
    	this.alliance = alliance;
    	// Command initialization
    	requires(drive = DriveSubsystem.getInstance());
    	setInterruptible(true);

		// Ensure the timer is initialized
		timer = null;
		
		// Initialize logging
		try{
			file = File.createTempFile("log_autondrive_", ".csv", new File("/home/lvuser"));
			print = new PrintWriter(new FileOutputStream(file.getAbsolutePath()));
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("DRIVE INIT");
    	
    	if (isFinished()) {        	
        	s_p_p = SmartDashboard.getNumber("Boiler Vertical", 0);
        	a_p_p = -1.05 * SmartDashboard.getNumber("Boiler Horizontal", 0);
        	
        	SmartDashboard.putNumber("a_p_p", a_p_p);
        	
        	startTime = System.currentTimeMillis();
        	
			timer = new Timer();
			timer.scheduleAtFixedRate(new CalculateClass(), 100,
					(long) (dt * 1000));

			// Reset sensors
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
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	long time = System.currentTimeMillis() - startTime;
    	/*if(time < 100) {
    		drive.setPower(0, 0);
    	}
    	else if(time < 300) {
    		drive.setPower(alliance ? 1 : -1, alliance ? -1 : 1);
    	}
    	else if(time < 400) {
    		drive.setPower(0, 0);
    	}
    	else if(time < 700) {
    		drive.setPower(!alliance ? 1 : -1, !alliance ? -1 : 1);
    	}
    	else if(time < 800){
    		drive.setPower(0, 0);
    	}
    	else if(time >= 5000) {
    		end();
    	}*/
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	System.out.println("DRIVE FINISHED?");
        return timer == null;
    }

    // May be called multiple times in this class
    protected void end() {
    	System.out.println("DRIVE END");
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
    		System.out.println("DRIVE_LOOP");
    		// ----------------------------------------
    		// Calculate for translational

			// Read the velocity
			double s_m_p = drive.getAveragePosition();
			System.out.println("                             " + s_p_p + " , " + s_m_p);
			// Proportional error
			s_ep = s_p_p - s_m_p;
			// Integral error
			s_ei += s_ep * dt;
			// Derivative error
			s_ed = (s_p - s_m_p) / dt;

			// Update the sensor variable
			s_p = s_m_p;
			
			// Calculate the Output Value
			double s_output = 0;
			s_output += Math.min(s_lim_p, Math.max(-s_lim_p, s_kp * s_ep));
			s_output += Math.min(s_lim_i, Math.max(-s_lim_i, s_ki * s_ei));
			s_output += Math.min(s_lim_d, Math.max(-s_lim_d, s_kd * s_ed));
			SmartDashboard.putNumber("New Translational PID Offset",s_ep);

			// Limit and scale the output
			s_output = Math.min(1, Math.max(-1, s_output)) * s_lim_q;

    		// ----------------------------------------			
			// Calculate for rotational
			
			// Read the heading
			double a_m_p = -drive.getGyro().getAngle();

			// Position error
			a_ep = a_p_p - a_m_p;
			// Integral error
			a_ei += a_ep * dt;
			// Derivative error
			a_ed = (a_p - a_m_p) / dt;
			
			SmartDashboard.putNumber("New Gyro PID Offset",a_ep);

			// Update the sensor variable
			a_p = a_m_p;
			
			// Calculate the Output Value
			double a_output = 0;
			a_output += Math.min(a_lim_p, Math.max(-a_lim_p, a_kp * a_ep));
			a_output += Math.min(a_lim_i, Math.max(-a_lim_i, a_ki * a_ei));
			a_output += Math.min(a_lim_d, Math.max(-a_lim_d, a_kd * a_ed));
			if(Math.abs(a_p_p) > deadband){
				a_output += offset * Math.signum(a_p_p);
			}

			// Limit and scale the output
			a_output = Math.min(1, Math.max(-1, a_output)) * a_lim_q;

			// Combine the outputs
			double outputL = Math.min(lim_q, Math.max(-lim_q, s_output - a_output));
			double outputR = Math.min(lim_q, Math.max(-lim_q, s_output + a_output));
			drive.setPower(outputL, outputR);

			// Logging
			if(print != null) {
				System.out.println("LOG");
				print.format("%.4f, %.4f, %.4f, %.4f\n", s_p_p, s_m_p, a_p_p, a_m_p);
			}
    	}
    }
}