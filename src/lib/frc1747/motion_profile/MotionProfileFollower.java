package lib.frc1747.motion_profile;

import java.util.Timer;
import java.util.TimerTask;

import lib.frc1747.pid.Feedback;
import lib.frc1747.pid.PIDValues;

public class MotionProfileFollower extends TimerTask {
	
	private Timer timer = null;
	
	private PIDValues pid;
	private Feedback feedback;
	
	private double period;
	private Double setPoint;
	
	public static final double DEFAULT_PERIOD = 1 / 100.0;
	
	private double sum/*, last*/;
	
	public MotionProfileFollower(PIDValues pid, double period) {
		this.pid = pid;
		this.period = period;
	}
	
	public MotionProfileFollower(PIDValues pid) {
		this(pid, DEFAULT_PERIOD);
	}
	
	public void setPeriod(double period) {
		if(timer == null) {
			this.period = period;
		} 
	}
	
	public void setSetpoint(double setpoint) {
		this.setPoint = setpoint;
	}
	
	public void setTrajectory(Trajectory left, Trajectory right) {
		
	}
	
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
	
	public void start() {
		if(timer == null && setPoint != null && feedback != null) {
			reset();
			timer = new Timer();
			timer.scheduleAtFixedRate(this, 0, (long) period);
		}
	}
	
	public void stop() {
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public void reset() {
		if(timer == null) {
			//last = 0;
			sum = 0;
		}
	}
	
	@Override
	public void run() {
		
		// Calculate the Output Value
		double output = 0;
		double position = feedback.getFeedback();
		double velo = feedback.getVelocityFeedback();
		
		output += pid.P * (setPoint - position); // P Term
		output += pid.I * (sum += position);	// I Term
		output += pid.D * velo;    // D Term
		
	//	output += pid.V;	// V Term
	//	output += pid.A;	// A Term
		
		//last = position; // Set feedback to last feed back
		
		feedback.setOutput(output);
	}
}
