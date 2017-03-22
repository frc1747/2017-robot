package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.pid.PIDValues;
import lib.frc1747.speed_controller.HBRTalon;
import lib.frc1747.subsystems.HBRSubsystem;

public class ConveyorSubsystem extends HBRSubsystem {

	public final double CONVEYOR_POWER = 0.85; 
	private final int 
		ENCODER_COUNTS_PER_REVOLUTION = 1/*,
		READ_TIME = 10*/;
	
	private HBRTalon motor1;
	private CANTalon motor2;
	private PIDValues pidValues = new PIDValues(5, 0.015, 200, 4.17);
	
	private static ConveyorSubsystem instance;

	private ConveyorSubsystem() {
		
		// Configure Motor 1
		motor1 = new HBRTalon(RobotMap.CONVEYOR_MOTOR1);
    	motor1.setInverted(RobotMap.CONVEYOR_INVERTED1);
		motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		motor1.reverseSensor(true);
		motor1.configNominalOutputVoltage(+0.0f, -0.0f);
		motor1.configPeakOutputVoltage(+12.0f, -12.0f);
		motor1.setProfile(0);
		motor1.setScaling(4 * ENCODER_COUNTS_PER_REVOLUTION);
		motor1.setNominalClosedLoopVoltage(12.0);
//		//System.out.println(motor1.getIZone());
		// Configure Motor 2
		motor2 = new CANTalon(RobotMap.CONVEYOR_MOTOR2);
    	motor2.setInverted(RobotMap.CONVEYOR_INVERTED2);
    	motor2.setNominalClosedLoopVoltage(12.0);
    	
    	setPIDF(pidValues);
		motor1.setIZone(20);
	}
	
	public void setPIDF(PIDValues pidValues){
		motor1.setPID(pidValues.P, pidValues.I, pidValues.D);
		motor1.setF(pidValues.F);
		
		this.pidValues = pidValues;
	}
	
	public static ConveyorSubsystem getInstance(){
		if (instance == null){
			instance = new ConveyorSubsystem();
		}
		return instance;
	}
	
	public void setMotorPower(double power) {		
		motor1.set(power);
		motor2.set(power);
	}
	
	public double getSpeed() {
		//return motor1.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
		return motor1.getSpeed();
	}
	
	public double getVoltage() {
		return motor1.getOutputVoltage();
	}
    
    public void enablePID(){
    	motor1.changeControlMode(TalonControlMode.Speed);
    	motor2.changeControlMode(TalonControlMode.Follower);
    }
    
    public void disablePID(){
    	motor1.changeControlMode(TalonControlMode.PercentVbus);
    	motor2.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void setSetpoint(double speed){
    	//speed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	motor1.set(speed);
    	motor2.set(motor1.getDeviceID());
    	//motor2.reverseOutput(true);
    }
	
	@Override
	protected void initDefaultCommand() {
		
	}

	@Override
	public void updateDashboard() {
		super.updateDashboard();
	}

	@Override
	public void debug() {
		SmartDashboard.putNumber("Conveyer Voltage", getVoltage());
		SmartDashboard.putNumber("Conveyer Speed", getSpeed());
	}
}
