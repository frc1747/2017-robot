package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;
import com.frc1747.commands.MotorTest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.subsystems.HBRSubsystem;



public class ShooterSubsystem extends HBRSubsystem {
	private final double READ_TIME = 0.1;
	private final double SHOOTER_DIAMETER = 1.6 / 12.0; //in feet
	private final double SHOOTER_CIRCUMFERENCE = SHOOTER_DIAMETER * Math.PI; 
	private final int ENCODER_COUNTS_PER_REVOLUTION = 6; // 6 cycles per 1 rev of shooter roller

	CANTalon topShooterMotor, bottomShooterMotor;
	double topP, topI, topD, topF;
	double bottomP, bottomI, bottomD, bottomF;
	private static ShooterSubsystem instance;
	
	public final double SHOOTER_POWER = 0; //TODO: put actual value
	
    private ShooterSubsystem(){
    	bottomShooterMotor = new CANTalon(RobotMap.BOTTOM_SHOOTER_MOTOR);
    	topShooterMotor = new CANTalon(RobotMap.TOP_SHOOTER_MOTOR);
		
		topShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//TODO: topShooterMotor.reverseSensor();
		topShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		//TODO: possibly +12.0f, -0.0f
		topShooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		topShooterMotor.configEncoderCodesPerRev(ENCODER_COUNTS_PER_REVOLUTION);
		topShooterMotor.setProfile(0);
		
		bottomShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//TODO: bottomShooterMotor.reverseSensor();
		bottomShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		//TODO: possibly +12.0f, -0.0f
		bottomShooterMotor.configPeakOutputVoltage(+12.0f, -0.0f);
		bottomShooterMotor.configEncoderCodesPerRev(ENCODER_COUNTS_PER_REVOLUTION);
		bottomShooterMotor.setProfile(0);
    	
		topD = 30;
		topF = 4.4;
		topP = 30;
    	topI = 0;
    	
    	topShooterMotor.setP(topP);
    	topShooterMotor.setI(topI);
    	topShooterMotor.setD(topD);
    	topShooterMotor.setF(topF);
    	
    	
    	bottomD = 20;
    	bottomF = 3.5;
    	bottomP = 18;
    	bottomI = 0;
    	
    	bottomShooterMotor.setP(bottomP);
    	bottomShooterMotor.setI(bottomI);
    	bottomShooterMotor.setD(bottomD);
    	bottomShooterMotor.setF(bottomF);
    }
    
    public static ShooterSubsystem getInstance(){
		if (instance == null){
			instance = new ShooterSubsystem();
		}
		return instance;
	}
    
    public void enablePID(){
    	topShooterMotor.changeControlMode(TalonControlMode.Speed);
    	bottomShooterMotor.changeControlMode(TalonControlMode.Speed);
    }
    
    public void disablePID(){
    	topShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	bottomShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void setSetpoint(double topSpeed, double bottomSpeed){
    	
    	topSpeed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	bottomSpeed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	
    	topShooterMotor.set(topSpeed);
    	bottomShooterMotor.set(bottomSpeed);
    }
    
    public void setTopPower(double power){
    	topShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	topShooterMotor.set(power);
    }
    
    public void setBottomPower(double power){
    	bottomShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	bottomShooterMotor.set(power);
    }
    
    public double getTopRPS(){
    	return topShooterMotor.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
    }
    
    public double getBottomRPS() {
    	return bottomShooterMotor.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
    }
    
    public double getTopPosition() {
    	return topShooterMotor.getPosition();
    }
    
    public double getBottomPosition() {
    	return bottomShooterMotor.getPosition();
    }

	public double getTopFeetPerSecond(){
		return (getTopRPS() * SHOOTER_CIRCUMFERENCE);
	}
	
	public double getBottomFeetPerSecond(){
		return (getBottomRPS() * SHOOTER_CIRCUMFERENCE);
	} 
    
	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    double avg_speed = 0;

	@Override
	public void updateDashboard() {
		SmartDashboard.putNumber("Top Encoder Speed", getTopRPS());
		SmartDashboard.putNumber("Top Shooter Speed (ft/s)", getTopFeetPerSecond());
		SmartDashboard.putNumber("Top Shooter Position", getTopPosition());
		avg_speed = avg_speed * .9 + getBottomRPS() * .1;
		SmartDashboard.putNumber("Bottom Encoder Filter Speed", avg_speed);
		SmartDashboard.putNumber("Bottom Encoder Speed", getBottomRPS());
		
		SmartDashboard.putNumber("Bottom Shooter Speed (ft/s)", avg_speed);
		SmartDashboard.putNumber("Bottom Encoder Position", getBottomPosition());
//		SmartDashboard.putNumber("Bottom Shooter Speed (ft/s)", getBottomFeetPerSecond());
	}
}

