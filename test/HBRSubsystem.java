import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public abstract class HBRSubsystem<E extends Enum<E>> {
	// Followers to use
	private E[] followers;
	private int n_followers;
	
	// Global parameters
	private double dt;
	private String name;
	private Timer timer;
	
	// Profile variables
	private double[][][] profile;
	private boolean[] running;
	private int[] index;
	private Mode[] mode;
	private PIDMode[] pidMode;
	
	// Feedforward constants
	private double[] kf_x;
	private double[] kf_v;
	private double[] kf_a;
	
	// Feedback constants
	private double[] kp;
	private double[] ki;
	private double[] kd;

	// Clamping variables
	private double[] lim_i;
	private double[] lim_q;
	
	// Sensor variables
	private double[] sensor;
	
	// Error variables
	private double[] ep;
	private double[] ei;
	private double[] ed;
	
	// Output variables
	private double[] output;
	
	protected HBRSubsystem(String name, double dt) {
		// Initialize subsystem
		super();
		
		// Set global parameters
		this.name = name;
		this.dt = dt;

		// Get the enum containing the systems to control (followers to use)
		Class<?> type = this.getClass();
		while(type.getSuperclass().getName() != "HBRSubsystem") {
			type = type.getSuperclass();
		}
		ParameterizedType superType = (ParameterizedType)type.getGenericSuperclass();
		Class<?> typeArguments = (Class<?>)superType.getActualTypeArguments()[0];
		followers = (E[])typeArguments.getEnumConstants();
		
		// Initialize PID/Follower only if there is at least one follower requested
		n_followers = followers.length;
		if(n_followers > 0) {
			// Initialize variables
			profile = new double[n_followers][][];
			running = new boolean[n_followers];
			index = new int[n_followers];
			mode = new Mode[n_followers];
			pidMode = new PIDMode[n_followers];
			kf_x = new double[n_followers];
			kf_v = new double[n_followers];
			kf_a = new double[n_followers];
			kp = new double[n_followers];
			ki = new double[n_followers];
			kd = new double[n_followers];
			lim_i = new double[n_followers];
			lim_q = new double[n_followers];
			sensor = new double[n_followers];
			ep = new double[n_followers];
			ei = new double[n_followers];
			ed = new double[n_followers];
			output = new double[n_followers];
			
			// Initialize loop
			timer = new Timer();
			timer.scheduleAtFixedRate(new Calculate(), 0, (long)(dt * 1000));
		}
	}
	
	public int getFollowerIndex(E follower) {
		return Arrays.asList(followers).indexOf(follower);
	}
	
	public void setFeedforward(E follower, double kf_x, double kf_v, double kf_a) {
		int i = getFollowerIndex(follower);
		this.kf_x[i] = kf_x;
		this.kf_v[i] = kf_v;
		this.kf_a[i] = kf_a;
	}
	
	public void setFeedback(E follower, double kp, double ki, double kd) {
		int i = getFollowerIndex(follower);
		this.kp[i] = kp;
		this.ki[i] = ki;
		this.kd[i] = kd;
	}
	
	public void setPIDMode(E follower, PIDMode pidMode) {
		int i = getFollowerIndex(follower);
		this.pidMode[i] = pidMode;
	}
	
	public void stop(E follower) {
		int i = getFollowerIndex(follower);
		this.running[i] = false;
	}
	
	public void start(E follower) {
		int i = getFollowerIndex(follower);
		this.running[i] = this.mode[i] == Mode.FOLLOWER;
	}
	
	public void setMode(E follower, Mode mode) {
		int i = getFollowerIndex(follower);
		
		// Save the mode
		this.mode[i] = mode;
		
		// Pause the profile for safety
		this.running[i] = false;
		this.index[i] = 0;
		
		// Ensure the profile variable is set correctly
		switch(mode) {
		case PID:
			this.profile[i] = new double[1][3];
			break;
		case FOLLOWER:
			this.profile[i] = null;
			break;
		}
	}
	
	public void setProfile(E follower, double profile[][]) {
		int i = getFollowerIndex(follower);
		
		// Save the profile
		this.profile[i] = profile;
		
		// Pause the profile for safety
		this.running[i] = false;
		this.index[i] = 0;
	}
	
	public void setSetpoint(E follower, double setpoint) {
		int i = getFollowerIndex(follower);
		
		// Save the setpoint data
		switch(this.pidMode[i]) {
		case POSITION:
			this.profile[i][0][0] = setpoint;
			break;
		case VELOCITY:
			this.profile[i][0][1] = setpoint;
			break;
		}
	}
	
	public abstract double[] pidRead();
	public abstract void pidWrite(double[] output);
	
	private class Calculate extends TimerTask {
		// Main calculation loop
		@Override
		public void run() {
			// Read in the sensors
			double[] pv = pidRead();
			
			// Process data
			for(int i = 0;i < n_followers;i++) {
				output[i] = 0;

				// Calculate feedforward
				output[i] += kf_x[i] * profile[i][index[i]][0];
				output[i] += kf_v[i] * profile[i][index[i]][1];
				output[i] += kf_a[i] * profile[i][index[i]][2];
				
				// Get the setpoint depending on the pid mode
				double sp = 0;
				switch(pidMode[i]) {
				case POSITION:
					sp = profile[i][index[i]][0];
					break;
				case VELOCITY:
					sp = profile[i][index[i]][1];
					break;
				}
				
				// Calculate errors
				// TODO: Use measured dt
				ep[i] = sp - pv[i];
				ei[i] += ep[i] * dt;
				ed[i] = (sensor[i] - pv[i]) / dt;
				
				// Limit integral
				if(lim_i[i] > 0) {
					if(ei[i] > lim_i[i]) ei[i] = lim_i[i];
					if(ei[i] < -lim_i[i]) ei[i] = -lim_i[i];
				}
				
				// Calculate feedback
				output[i] += kp[i] * ep[i];
				output[i] += ki[i] * ei[i];
				output[i] += kd[i] * ed[i];
				
				// Limit output
				if(lim_q[i] > 0) {
					if(output[i] > lim_q[i]) output[i] = lim_q[i];
					if(output[i] < -lim_q[i]) output[i] = -lim_q[i];
				}
			}
			
			// Update sensor values
			sensor = pv;
			
			// Write out the results
			pidWrite(output);
		}
	}
	
	public enum Mode {
		PID, FOLLOWER
	}
	
	public enum PIDMode {
		POSITION, VELOCITY
	}
}
