package lib.frc1747.speed_controller;

import com.ctre.CANTalon;

public class HBRTalon extends CANTalon {
	private static final double READ_TIME = 0.1; // Seconds
	
	private double scaling = 1;
	private boolean sensorReversed = false;

	public HBRTalon(int deviceNumber) {
		super(deviceNumber);
	}

	public HBRTalon(int deviceNumber, int controlPeriodMs) {
		super(deviceNumber, controlPeriodMs);
	}

	public HBRTalon(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
		super(deviceNumber, controlPeriodMs, enablePeriodMs);
	}

	@Override
	public String getSmartDashboardType() {
		return "HBRTalon";
	}

	@Override
	public void reverseSensor(boolean flip) {
		sensorReversed = flip;
		super.reverseSensor(flip);
	}
	
	/**
	 * Get the speed of the device (units per second)
	 * If scaling is used, this method will return the scaled units per second.
	 * @return scaled units per second
	 */
	@Override
	public double getSpeed() {
		double speed = super.getSpeed() / (READ_TIME * scaling * 4); 
		return speed;
	}
	@Override
	public double getPosition(){
		return super.getPosition() / (scaling * 4);
	}
	/**
	 * Refer to CTRE javadocs if used with non-SPEED and non-POSITION modes.
	 * In position mode, set the param as the desired scaled units.
	 * In speed mode, set the param as the scaled units per SECOND.
	 * @param outputValue the desired output of the Talon (depends on the current Talon Control Mode)
	 */
	@Override
	public void set(double outputValue) {
		if(super.getControlMode() == TalonControlMode.Speed) {
			outputValue *= (scaling * READ_TIME * 4);
		}
		else if(super.getControlMode() == TalonControlMode.Position) {
			outputValue *= scaling * 4; // fix this
		}
		super.set(outputValue);
	}

	/**
	   * Set the scaling of the encoder.
	   * @param scaling the value to be used when scaling the Talon's native units
	   */
	public void setScaling(double scaling) {
		this.scaling = scaling;
	}
	
	/**
	 * Gets the currently set scaling value. If scaling is not currently set, a 1 will be returned.
	 * @return scaling the value used for scaling the Talon's native units
	 */
	public double getScaling() {
		return scaling;
	}

	/**
	 * Returns whether or not the sensor is reversed.
	 * @return sensorReversed True if the sensor is reversed. False if the sensor is not reversed.
	 */
	public boolean getSensorReversed() {
		return sensorReversed;
	}
	
	/**
	 * Sets PIDF constants on the Talon.
	 * @param kP Proportion constant
	 * @param kI Integral constant
	 * @param kD Derivative constant
	 * @param kF Feed Forward constant
	 */
	public void setPIDF(double kP, double kI, double kD, double kF){
		super.setP(kP);
		super.setI(kI);
		super.setD(kD);
		super.setF(kF);
	}
}