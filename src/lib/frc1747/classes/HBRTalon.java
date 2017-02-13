package lib.frc1747.classes;

import com.ctre.CANTalon;

public class HBRTalon extends CANTalon {
	private static final double READ_TIME = 0.1; // Seconds
	
	private double scaling = 1;
	private boolean sensorReversed = false;

	public HBRTalon(int deviceNumber) {
		super(deviceNumber);
		this.configEncoderCodesPerRev(1);
	}

	public HBRTalon(int deviceNumber, int controlPeriodMs) {
		super(deviceNumber, controlPeriodMs);
		this.configEncoderCodesPerRev(1);
	}

	public HBRTalon(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
		super(deviceNumber, controlPeriodMs, enablePeriodMs);
		this.configEncoderCodesPerRev(1);
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
	
	@Override
	public double getSpeed() {
		double speed = super.getSpeed() / (scaling / READ_TIME); 
		return speed;
	}
	
	@Override
	public void set(double input) {
		if(super.getControlMode() == TalonControlMode.Speed) {
			input *= (scaling / READ_TIME);
		}
		else if(super.getControlMode() == TalonControlMode.Position) {
			input *= scaling; // fix this
		}
		super.set(input);
	}

	public void setScaling(double scaling) {
		this.scaling = scaling;
	}
	
	public double getScaling() {
		return scaling;
	}

	public boolean getSensorReversed() {
		return sensorReversed;
	}
}