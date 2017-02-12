package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;
import com.frc1747.commands.conveyer.ConveyIn;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.subsystems.HBRSubsystem;

public class ConveyorSubsystem extends HBRSubsystem {

	private CANTalon motor1;
	private CANTalon motor2;
	private static ConveyorSubsystem instance;
	public final double CONVEYOR_POWER = 0.85; //TODO: use actual power
	private final int ENCODER_COUNTS_PER_REVOLUTION = 120;
	private final int READ_TIME = 10;
	double kP, kI, kD, kF;
	
	private ConveyorSubsystem() {
		motor1 = new CANTalon(RobotMap.CONVEYOR_MOTOR1);
		motor2 = new CANTalon(RobotMap.CONVEYOR_MOTOR2);
    	motor1.setInverted(RobotMap.CONVEYOR_INVERTED1);
    	motor2.setInverted(RobotMap.CONVEYOR_INVERTED2);
		motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		motor1.reverseSensor(false);
		motor1.configNominalOutputVoltage(+0.0f, -0.0f);
		motor1.configPeakOutputVoltage(+12.0f, -12.0f);
		motor1.configEncoderCodesPerRev(ENCODER_COUNTS_PER_REVOLUTION);
		motor1.setProfile(0);
		
		kP = 0;
		kI = 0;
		kD = 0;
		kF = 0;
	}
	
	public void setPIDF(double kP, double kI, double kD, double kF){
		motor1.setP(kP);
		motor1.setI(kI);
		motor1.setD(kD);
		motor1.setF(kF);
	}
	
	public static ConveyorSubsystem getInstance(){
		if (instance == null){
			instance = new ConveyorSubsystem();
		}
		return instance;
	}
	
	public void setMotorPower(double power) {		
		//motor1.set(power);
		//motor2.set(power);
	}
	
	public double getSpeed() {
		//return motor1.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
		return motor1.getSpeed();
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
    	speed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	motor1.set(speed);
    	motor2.set(motor1.getDeviceID());
    }
	
	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public void updateDashboard() {
		SmartDashboard.putNumber("Conveyer Speed", getSpeed());
	}
}
