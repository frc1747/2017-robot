package lib.frc1747.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;
import lib.frc1747.motion_profile.Parameters;
import lib.frc1747.motion_profile.generator._1d.ProfileGenerator;
import lib.frc1747.motion_profile.generator._2d.SplineGenerator;
import lib.frc1747.motion_profile.gui._1d.BoxcarFilter;

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
public abstract class HBRSubsystem<E extends Enum<E>> extends Subsystem {
	Logger logger;
	PrintWriter print;
	
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
	private boolean enabled;
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
	
	/**
	 * Creates a new HBRSubsystem. The name is by default derived from the class name.
	 * @param dt - the timestep to use for PID and motion profiling (rounded to the nearest millisecond)
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
	 * @param dt - the timestep to use for PID and motion profiling (rounded to the nearest millisecond)
	 */
	protected HBRSubsystem(String name, double dt) {
		
		
		// Initialize subsystem
		super();
		
		try{
			print = new PrintWriter(
					new FileOutputStream(
					File.createTempFile("log_", ".csv",
							new File("/home/lvuser")), true));
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		
		logger = Instrumentation.getLogger("Drive Subsystem");
		logger.registerDouble("ProfileDistance", true, true);
		logger.registerDouble("ActualDistance", true, true);
		logger.registerDouble("ProfileAngle", true, true);
		logger.registerDouble("ActualAngle", true, true);
		
		// Maintaining compatibility with previous versions
		subsystems.add(this);
		
		// Error if anything is unreasonable
		if(!(dt >= 0)) {
			throw new IllegalArgumentException(dt + " is not a valid time step (must be positive or zero).");
		}
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
		while(!type.getSuperclass().getName().equals(HBRSubsystem.class.getName())) {
			type = type.getSuperclass();
		}
		if(type.getGenericSuperclass() instanceof ParameterizedType) {
			ParameterizedType superType = (ParameterizedType)type.getGenericSuperclass();
			Class<?> typeArguments = (Class<?>)superType.getActualTypeArguments()[0];
			followers = (E[])typeArguments.getEnumConstants();
			n_followers = followers.length;
		}
		else {
			n_followers = 0;
		}
		
		// Initialize PID/Follower only if there is at least one follower requested
		if(n_followers > 0) {
			// Initialize variables
			profile = new double[n_followers][][];
			running = new boolean[n_followers];
			enabled = false;
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
			
			// Set up reasonable defaults
			for(int i = 0;i < n_followers;i++) {
				setMode(followers[i], Mode.PID);
				setPIDMode(followers[i], PIDMode.POSITION);
			}
			
			// Initialize loop
			timer = new Timer();
			timer.scheduleAtFixedRate(new Calculate(), 1000, (long)Math.round(dt * 1000));
			oldTime = System.nanoTime();
		}
	}
	
	/**
	 * Returns a constant index for the specified PID/follower. Useful for the pidRead and pidWrite methods.
	 * @param follower - the PID/follower for which to get the index
	 * @return the constant index that is used to refer to the profile follower internally
	 */
	public int getFollowerIndex(E follower) {
		if(n_followers <= 0) {
			throw new IllegalStateException("There are no PID/followers in this subsystem to control.");
		}
		int i = Arrays.asList(followers).indexOf(follower);
		if(i < 0) {
			throw new IllegalArgumentException(follower + " is not a valid PID/follower in this subssytem.");
		}
		return i;
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
		if(!(limit >= 0)) {
			throw new IllegalArgumentException(limit + " is not a valid integral limit (must be positive or zero).");
		}
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
		if(!(limit >= 0)) {
			throw new IllegalArgumentException(limit + " is not a valid output limit (must be positive or zero).");
		}
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
	 * @param follower - which PID/follower to use when stopping
	 */
	public void pause(E follower) {
		int i = getFollowerIndex(follower);
		if(!(this.mode[i] == Mode.FOLLOWER)) {
			throw new IllegalStateException("pause can only be called when in motion profile follower mode.");
		}
		this.running[i] = false;
	}
	
	/**
	 * Resumes the execution of one PID/follower when it is in motion profile follower mode.
	 * @param follower - which PID/follower to use when starting
	 */
	public void resume(E follower) {
		int i = getFollowerIndex(follower);
		if(!(this.mode[i] == Mode.FOLLOWER)) {
			throw new IllegalStateException("resume can only be called when in motion profile follower mode.");
		}
		this.running[i] = true;
	}
	
	/**
	 * Rewinds the to the start of a profile for a single PID/follower if it is in motion profile follower mode.
	 * @param follower - which PID/follower to use when rewinding
	 */
	public void rewind(E follower) {
		int i = getFollowerIndex(follower);
		if(!(this.mode[i] == Mode.FOLLOWER)) {
			throw new IllegalStateException("rewind can only be called when in motion profile follower mode.");
		}
		this.index[i] = 0;
	}

	/**
	 * Checks to see if a single PID/follower is running if it is in motion profile follower mode.
	 * @param follower - which PID/follower to use when checking state
	 * @return if the motion profile follower is running
	 */
	public boolean isRunning(E follower) {
		int i = getFollowerIndex(follower);
		if(!(this.mode[i] == Mode.FOLLOWER)) {
			throw new IllegalStateException("isRunning can only be called when in motion profile follower mode.");
		}
		return this.running[i];
	}
	
	/**
	 * Sets a specific PID/follower to use either position or velocity PID.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param pidMode - either position or velocity mode
	 */
	public void setPIDMode(E follower, PIDMode pidMode) {
		int i = getFollowerIndex(follower);

		// Set the pid mode
		int j = Arrays.asList(PIDMode.values()).indexOf(pidMode);
		if(j < 0) {
			throw new IllegalArgumentException(pidMode + " is not a valid PID mode.");
		}
		this.pidMode[i] = pidMode;
	}
	
	/**
	 * Checks if a specific PID/follower is using position or velocity PID.
	 * @param follower - which PID/follower to use when getting this parameter
	 * @return if the specified PID/follower is in position or velocity mode
	 */
	public PIDMode getPIDMode(E follower) {
		int i = getFollowerIndex(follower);
		return this.pidMode[i];
	}
	
	/**
	 * Sets a specific PID/follower to either PID mode or motion profile follower mode.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param mode - either PID mode or motion profile follower mode
	 */
	public void setMode(E follower, Mode mode) {
		int i = getFollowerIndex(follower);
		
		// Set the mode
		int j = Arrays.asList(Mode.values()).indexOf(mode);
		if(j < 0) {
			throw new IllegalArgumentException(mode + " is not a valid PID/follower mode.");
		}
		this.mode[i] = mode;
		
		// Ensure a profile variable exists
		this.profile[i] = new double[1][3];
		this.index[i] = 0;
	}
	
	/**
	 * Checks if a specific PID/follower is in PID mode or motion profile follower mode.
	 * @param follower - which PID/follower to use when getting this parameter
	 * @return mode - if the specified PID/follower is in PID mode or motion profile follower mode.
	 */
	public Mode getMode(E follower) {
		int i = getFollowerIndex(follower);
		return this.mode[i];
	}
	
	/**
	 * Sets a motion profile to follow for a specific PID/follower.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param profile - the profile to follower<br>
	 * The format is [x0, v0, a0; x1, v1, a1; ...]
	 */
	public void setProfile(E follower, double profile[][]) {
		int i = getFollowerIndex(follower);
		if(!(this.mode[i] == Mode.FOLLOWER)) {
			throw new IllegalStateException("setProfile can only be called when in motion profile follower mode.");
		}
		this.profile[i] = profile;
	}
	
	/**
	 * Sets a setpoint for a specific PID/follower.
	 * @param follower - which PID/follower to use when setting this parameter
	 * @param setpoint - the position or velocity setpoint depending on the current PID mode
	 */
	public void setSetpoint(E follower, double setpoint) {
		int i = getFollowerIndex(follower);

		if(!(this.mode[i] == Mode.PID)) {
			throw new IllegalStateException("setSetpoint can only be called when in PID mode.");
		}
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
	 * Enables or disables all PID/followers
	 * @param enabled if the PID/followers should be enabled or disabled
	 */
	public void setEnabled(boolean enabled) {
		// Need to run some initialization of we are enabling
		if(enabled && !this.enabled) {
			double[][] pv_raw = internalPidRead();
			for(int i = 0;i < n_followers;i++) {
				switch(pidMode[i]) {
				case POSITION:
					sensor[i] = pv_raw[0][i];
					break;
				case VELOCITY:
					sensor[i] = pv_raw[1][i];
					break;
				}
			}
		}
		
		// Save the state
		this.enabled = enabled;
	}
	
	/**
	 * An abstract method that needs to be implemented in order to send data to the PID/followers.
	 * @return an array of sensor inputs to be consumed by the PID/followers<br>
	 * The first set of values are the position values, and the second set of values are velocity values.
	 * The size is expected to match the number of profiles. The index of each profile
	 * can be determined with getFollowerIndex
	 */
	public abstract double[][] pidRead();
	
	/**
	 * An internal method that gets sensor data from pidRead and performs some validation
	 * @return an array of sensor inputs to be consumed by the PID/followers
	 */
	private double[][] internalPidRead() {
		double[][] pv = pidRead();
		if(pv == null) {
			throw new IllegalArgumentException("The sensor array returned by pidRead cannot be null.");
		}
		if(pv.length != 2) {
			throw new IllegalArgumentException("The sensor array must include both position and velocity components.");
		}
		if(pv[0].length != n_followers) {
			throw new IllegalArgumentException("The number of sensors returned by pidRead must match the number of PID/followers.");
		}
		return pv;
	}
	
	/**
	 * An abstract method that needs to be implemented in order to recieve data from the PID/followers.
	 * @param output - the outputs of the PID/followers<br>
	 * The index of each profile can be determined with getFollowerIndex
	 */
	public abstract void pidWrite(double[] output);
	
	/**
	 * Attempts to read a motion profile from a file.
	 * @param filename - the file to read the profile from
	 * @return an array containing the profile, or null if there was an error reading the file
	 */
	public static double[][][] readProfilesFromFile(String filename) {
		double[][][] profile = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String[] parts = br.readLine().split(",");
			int count = Integer.parseInt(parts[0].trim());	// Profile count
			int length = Integer.parseInt(parts[1].trim());	// Number of time points
			
			profile = new double[count][length][3];
			
			for(int i = 0;i < length;i++) {
				parts = br.readLine().split(",");
				for(int j = 0;j < count;j++) {
					for(int k = 0;k < 3;k++) {
						profile[j][i][k] = Double.parseDouble(parts[j * 3 + k].trim());
					}
				}
			}
			br.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return profile;
	}
	
	/**
	 * Creates a motion profile for a skid steer robot to
	 * drive the specified distance and turn the specified angle.
	 * 
	 * Both measurements are relative and the turn angle is counterclockwise positive.
	 * 
	 * @param ds the distance to drive (ft)
	 * @param dtheta the angle to turn (rad)
	 * @return a profile for a skid steer robot to execute both the driving and turning
	 * at the same time
	 */
	public static double[][][] generateSkidSteerPseudoProfile(double ds, double dtheta) {
		// Ensure the profile generator does not freak out
		final double min_ds = 0.0100001;
		if(ds >= 0 & ds < min_ds) ds = min_ds;
		if(ds < 0 & ds > -min_ds) ds = -min_ds;
		
		double[][] profileSegments = SplineGenerator.flattenPseudoProfile(
				0, 0, ds, dtheta,
				Parameters.I_SAMPLE_LENGTH);
		
		double[][] profilePoints = ProfileGenerator.primaryProfileIntegrate(profileSegments, 0);
		double[] angularProfilePoints = ProfileGenerator.secondaryProfileIntegrate(profileSegments, 1);
		
		ProfileGenerator.skidSteerLimitVelocities(profilePoints, profileSegments,
				Parameters.V_MAX, Parameters.A_MAX, Parameters.W_WIDTH);
		
		// Force the max everything at the endpoints of the profile to zero
		profilePoints[0][1] = 0;
		profilePoints[0][2] = 0;
		profilePoints[profilePoints.length-1][1] = 0;
		profilePoints[profilePoints.length-1][2] = 0;
		
		ProfileGenerator.limitVelocities(profilePoints);
		double[] profileTimes = ProfileGenerator.timesFromPoints(profilePoints);
		double[][] timePoints = ProfileGenerator.profileFromPoints(profilePoints, profileTimes, Parameters.DT);
		double[][] angularTimePoints = ProfileGenerator.synchronizedProfileFromProfile(timePoints,
				profilePoints,
				angularProfilePoints,
				profileTimes,
				Parameters.DT);
		
		// Limit the maximum jerk
		double jerkFilterTime = Parameters.A_MAX/Parameters.J_MAX;
		timePoints = BoxcarFilter.multiFilter(timePoints, (int)Math.ceil(jerkFilterTime/Parameters.DT));
		angularTimePoints = BoxcarFilter.multiFilter(angularTimePoints, (int)Math.ceil(jerkFilterTime/Parameters.DT));
		
		return new double[][][] {timePoints, angularTimePoints};
	}
	
	/**
	 * Internal class to actually calculate the PID/follower stuff.
	 * @author Tiger
	 *
	 */
	private class Calculate extends TimerTask {
		// Main calculation loop
		@Override
		public void run() {
			// Do nothing if we are not enabled
			if(!enabled) return;
			
			// Read in the sensors
			double[][] pv_raw = internalPidRead();
			double[] pv = new double[n_followers];
			double[] setPoints = new double[n_followers];
			
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
				
				// Get the setpoint and process variable depending on the pid mode
				double sp = 0;
				switch(pidMode[i]) {
				case POSITION:
					sp = profile[i][index[i]][0];
					pv[i] = pv_raw[0][i];
					break;
				case VELOCITY:
					sp = profile[i][index[i]][1];
					pv[i] = pv_raw[1][i];
					break;
				}
				setPoints[i] = sp;
				
//				System.out.println(pv[i] + "," + sp);
				
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
			if(print != null) {
				print.format("%.4f, %.4f, %.4f, %.4f\n", setPoints[0], pv[0], setPoints[1], pv[1]);
				//				//System.out.println("LOG")
				print.flush();
			}
			
			logger.putDouble("ActualDistance", pv[0]);
			logger.putDouble("ActualAngle", pv[1]);
			logger.putDouble("ProfileDistance", setPoints[0]);
			logger.putDouble("ProfileAngle", setPoints[1]);
			// Update sensor values
			sensor = pv;
			
			// Write out the results
			pidWrite(output);
		}
	}

	/*
	 * Maintaining compatibility with previous version
	 */
	private static ArrayList<HBRSubsystem> subsystems = new ArrayList<>();
	public void debug() {}
	public void updateDashboard() {
		debug();
	}
	public static void update() {
		for(HBRSubsystem subsystem : subsystems)
			subsystem.updateDashboard();
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
}
