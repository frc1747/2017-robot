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
	private static ConveyorSubsystem instance;
	public final double CONVEYOR_POWER = 0.75; //TODO: use actual power
	private final int ENCODER_COUNTS_PER_REVOLUTION = 1;
	private final int READ_TIME = 10;
	
	private ConveyorSubsystem() {
		//motor1 = new CANTalon(RobotMap.CONVEYOR_MOTOR);
		motor1 = new CANTalon(RobotMap.CLIMBER_MOTOR1);
    	motor1.setInverted(true);
		motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		motor1.reverseSensor(false);
		motor1.configNominalOutputVoltage(+0.0f, -0.0f);
		motor1.configPeakOutputVoltage(+12.0f, -12.0f);
		motor1.configEncoderCodesPerRev(12);
		motor1.setProfile(0);
	}
	
	public static ConveyorSubsystem getInstance(){
		if (instance == null){
			instance = new ConveyorSubsystem();
		}
		return instance;
	}
	
	public void setMotorPower(double power) {		
		motor1.set(power);
	}
	
	public double getSpeed() {
		return motor1.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
	}
    
    public void enablePID(){
    	motor1.changeControlMode(TalonControlMode.Speed);
    }
    
    public void disablePID(){
    	motor1.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void setSetpoint(double speed){
    	speed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	motor1.set(speed);
    }
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		//setDefaultCommand(new ConveyIn());
	}

	@Override
	public void updateDashboard() {
		// TODO Auto-generated method stub
		SmartDashboard.putNumber("Conveyer Speed", getSpeed());
	}
}
