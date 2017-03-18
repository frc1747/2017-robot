import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * A subclass of Subsystem written for 1747 that includes extra features such as
 * multiple motion profiles and PID loops with feed forward.
 * 
 * Examples of how this class is used can be found in the tests directory.
 * 
 * @author Tiger
 *
 * @param <E> An enum that lists the different PID/followers (e.g. distance & angle).
 */
public abstract class HBRSubsystem<E extends Enum<E>> implements LiveWindowSendable {
	// Followers to use
	private E[] followers;
	private int n_followers;
	
	// Global parameters
	private double dt;
	private String name;
	private Timer timer;
	private long oldTime;
	
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
	
	private ITable table;
	
	/**
	 * Creates a new HBRSubsystem. The name is by default derived from the class name.
	 * @param dt - the timestep to use for PID and motion profiling
	 */
	protected HBRSubsystem(double dt) {
		this(null, dt);
	}
	
	/**
	 * Creates a new HBRSubsystem. 
	 * The time step is a default of 0.01s.
	 * The name is by default derived from the class name.
	 */
	protected HBRSubsystem() {
		this(null, 0);
	}
	
	/**
	 * Creates a new HBRSubsystem. The time step is a default of 0.01s.
	 * @param name - the name to use for logging
	 */
	protected HBRSubsystem(String name) {
		this(name, 0);
	}
	
	/**
	 * Creates a new HBRSubsystem
	 * @param name - the name to use for logging
	 * @param dt - the timestep to use for PID and motion profiling
	 */
	protected HBRSubsystem(String name, double dt) {
		// Initialize subsystem
		super();
		
		// Set some reasonable defaults
		if(name == null) {
			name = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		}
		if(dt == 0) {
			dt = 0.01;
		}
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
		n_followers = followers.length;
		
		// Initialize PID/Follower only if there is at least one follower requested
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
			oldTime = System.nanoTime();
		}
	}
	
	/**
	 * Returns a constant index for the specified PID/follower. Useful for the pidRead and pidWrite methods.
	 * @param follower - the PID/follower for which to get the index
	 * @return the constant index that is used to refer to the profile follower internally
	 */
	public int getFollowerIndex(E follower) {
		return Arrays.asList(followers).indexOf(follower);
	}
	
	/**
	 * Sets the feed forward constants for one PID/follower.
	 * @param follower - which PID/follower to use when setting the constants
	 * @param kf_x - position feed forward constant
	 * @param kf_v - velocity feed forward constant
	 * @param kf_a - acceleration feed forward constant
	 */
	public void setFeedforward(E follower, double kf_x, double kf_v, double kf_a) {
		int i = getFollowerIndex(follower);
		this.kf_x[i] = kf_x;
		this.kf_v[i] = kf_v;
		this.kf_a[i] = kf_a;
	}
	
	/**
	 * Sets the PID feedback constants for one PID/follower.
	 * @param follower - which PID/follower to use when setting the constants
	 * @param kp - proportional feedback constant
	 * @param ki - integral feedback constant
	 * @param kd - derivative feedback constant
	 */
	public void setFeedback(E follower, double kp, double ki, double kd) {
		int i = getFollowerIndex(follower);
		this.kp[i] = kp;
		this.ki[i] = ki;
		this.kd[i] = kd;
	}
	
	/**
	 * Sets the limit for the integral accumulator for one PID/follower.
	 * @param follower - which PID/follower to use when setting the constants
	 * @param limit - the maximum values the integral accumulator is allowed to reach<br>
	 * Set to 0 to disable the integral accumulator limit
	 */
	public void setILimit(E follower, double limit) {
		int i = getFollowerIndex(follower);
		this.lim_i[i] = limit;
	}
	
	/**
	 * Sets the limit for the output of one PID/follower.
	 * @param follower - which PID/follower to use when setting the constants
	 * @param limit - the maximum values the output is allowed to reach<br>
	 * Set to 0 to disable the output limit
	 */
	public void setOutputLimit(E follower, double limit) {
		int i = getFollowerIndex(follower);
		this.lim_q[i] = limit;
	}
	
	/**
	 * Zeros the integrator for one PID/follower.
	 * @param follower - which PID/follower to use when setting this parameter
	 */
	public void resetIntegrator(E follower) {
		int i = getFollowerIndex(follower);
		this.ei[i] = 0;
	}
	
	/**
	 * Pauses the execution of one PID/follower when it is in motion profile follower mode.
	 * Does nothing if the PID/follower is in PID mode.
	 * @param follower - which PID/follower to use when stopping
	 */
	public void pause(E follower) {
		int i = getFollowerIndex(follower);
		this.running[i] = false;
	}
	
	/**
	 * Resumes the execution of one PID/follower when it is in motion profile follower mode.
	 * Does nothing if the PID/follower is in PID mode.
	 * @param follower - which PID/follower to use when starting
	 */
	public void resume(E follower) {
		int i = getFollowerIndex(follower);
		this.running[i] = this.mode[i] == Mode.FOLLOWER;
	}
	
	/**
	 * Rewinds the to the start of a profile for a single PID/follower if it is in motion profile follower mode.
	 * Does nothing if the PID/follower is in PID mode.
	 * @param follower - which PID/follower to use when rewinding
	 */
	public void rewind(E follower) {
		int i = getFollowerIndex(follower);
		this.index[i] = 0;
	}
	
	/**
	 * Sets a specific PID/follower use either position or velocity PID.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param pidMode - either position or velocity mode
	 */
	public void setPIDMode(E follower, PIDMode pidMode) {
		int i = getFollowerIndex(follower);
		this.pidMode[i] = pidMode;
		
		// Prevent a spike in the derivative reading
		sensor = pidRead();
	}
	
	/**
	 * Sets a specific PID/follower to either PID mode or motion profile follower mode.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param mode - either PID mode or motion profile follower mode
	 */
	public void setMode(E follower, Mode mode) {
		int i = getFollowerIndex(follower);
		this.mode[i] = mode;
		
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
	
	/**
	 * Sets a motion profile to follow for a specific PID/follower.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param profile - the profile to follower<br>
	 * The format is [x0, v0, a0; x1, v1, a1; ...]
	 */
	public void setProfile(E follower, double profile[][]) {
		int i = getFollowerIndex(follower);
		this.profile[i] = profile;
	}
	
	/**
	 * Sets a setpoint for a specific PID/follower.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param setpoint - the position or velocity setpoint depending on the current PID mode
	 */
	public void setSetpoint(E follower, double setpoint) {
		int i = getFollowerIndex(follower);
		
		// Save the setpoint data
		switch(this.pidMode[i]) {
		case POSITION:
			this.profile[i][0][0] = setpoint;
			this.profile[i][0][1] = 0;
			this.profile[i][0][2] = 0;
			break;
		case VELOCITY:
			this.profile[i][0][1] = setpoint;
			this.profile[i][0][0] = 0;
			this.profile[i][0][2] = 0;
			break;
		}
	}
	
	/**
	 * An abstract method that needs to be implemented in order to send data to the PID/followers.
	 * @return an array of sensor inputs to be consumed by the PID/followers<br>
	 * The size is expected to match the number of profiles. The index of each profile
	 * can be determined with getFollowerIndex
	 */
	public abstract double[] pidRead();
	
	/**
	 * An abstract method that needs to be implemented in order to recieve data from the PID/followers.
	 * @param output - the outputs of the PID/followers<br>
	 * The index of each profile can be determined with getFollowerIndex
	 */
	public abstract void pidWrite(double[] output);
	
	/**
	 * Internal class to actually calculate the PID/follower stuff.
	 * @author Tiger
	 *
	 */
	private class Calculate extends TimerTask {
		// Main calculation loop
		@Override
		public void run() {
			// Read in the sensors
			double[] pv = pidRead();
			
			// Calculate delta time
			long time = System.nanoTime();
			double deltaT = (time - oldTime)/1000000000.;
			oldTime = time;
			
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
				ep[i] = sp - pv[i];
				ei[i] += ep[i] * deltaT;
				ed[i] = (sensor[i] - pv[i]) / deltaT;
				
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
				
				// Update index
				if(running[i]) {
					index[i]++;
				}
				
				// Stop the profile if the end is reached
				if(index[i] > profile[i].length-1) {
					index[i] = profile[i].length-1;
					running[i] = false;
				}
			}
			
			// Update sensor values
			sensor = pv;
			
			// Write out the results
			pidWrite(output);
		}
	}
	
	/**
	 * Possible modes for the PID/Follower.<br>
	 * PID mode runs just a PID with feedforward.<br>
	 * Follower mode follows a motion profile that is provided.
	 * @author Tiger
	 *
	 */
	public enum Mode {
		PID, FOLLOWER
	}
	
	/**
	 * Possible modes for the PID controller.<br>
	 * Position mode runs the PID loop for position.<br>
	 * Velocity mode runs the PID loop for Velocity.
	 * @author Tiger
	 *
	 */
	public enum PIDMode {
		POSITION, VELOCITY
	}
	
	@Override
	public void updateTable() {
	}

	@Override
	public void startLiveWindowMode() {
	}

	@Override
	public void stopLiveWindowMode() {
	}
	
	private final ITableListener listener = (table, key, value, isNew) -> {
		//TODO fix this section
		for(int i = 0; i < n_followers; i++) {
			if(key.equals("kp" + i) || key.equals("ki" + i) || key.equals("kd" + i)) {
				if(this.kp[i] != table.getNumber("kp" + i, 0.0)
						|| this.ki[i] != table.getNumber("ki" + i, 0.0)
						|| this.kd[i] != table.getNumber("kd" + i, 0.0)) {
					setFeedback(followers[i], table.getNumber("kp" + i, 0.0), table.getNumber("ki" + i, 0.0), table.getNumber("kd" + i, 0.0));
				}
				if(this.kf_x[i] != table.getNumber("kf_x" + i, 0.0)
						|| this.kf_v[i] != table.getNumber("kf_v" + i, 0.0)
						|| this.kf_a[i] != table.getNumber("kf_a" + i, 0.0)) {
					setFeedforward(followers[i], table.getNumber("kf_x" + i, 0.0), table.getNumber("kf_v" + i, 0.0), table.getNumber("kf_a" + i, 0.0));
				}
			}
		}
	};

	@Override
	public void initTable(ITable table) {
		if (this.table != null) {
			table.removeTableListener(listener);
		}
		this.table = table;
		if(table != null) {
			//TODO fix this section
			for(int i = 0; i < n_followers; i++) {
				// I have no idea if this is correct but whatever
				table.putString("Follower" + i, followers[i].toString());
				table.putNumber("kp" + i, this.kp[i]);
				table.putNumber("ki" + i, this.ki[i]);
				table.putNumber("kd" + i, this.kd[i]);
				table.putNumber("kf_x" + i, this.kf_x[i]);
				table.putNumber("kf_v" + i, this.kf_v[i]);
				table.putNumber("kf_a" + i, this.kf_a[i]);
			}
		}
		table.addTableListener(listener, false);
	}

	@Override
	public ITable getTable() {
		return table;
	}

	@Override
	public String getSmartDashboardType() {
		return "Subsystem";
	}
}
