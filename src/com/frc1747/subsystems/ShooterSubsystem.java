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

	CANTalon backShooterMotor, frontShooterMotor;
	double topP, topI, topD, topF;
	double bottomP, bottomI, bottomD, bottomF;
	private static ShooterSubsystem instance;
	
	public final double SHOOTER_POWER = 0; //TODO: put actual value
	
    private ShooterSubsystem(){
    	backShooterMotor = new CANTalon(RobotMap.TOP_SHOOTER_MOTOR);
    	//bottomShooterMotor = new CANTalon(RobotMap.BOTTOM_SHOOTER_MOTOR);
    	frontShooterMotor = new CANTalon(RobotMap.CLIMBER_MOTOR2);
    	
		backShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		backShooterMotor.reverseSensor(true);
		backShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		backShooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		backShooterMotor.configEncoderCodesPerRev(ENCODER_COUNTS_PER_REVOLUTION);
		backShooterMotor.setProfile(0);
		
		frontShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontShooterMotor.reverseSensor(true);
		frontShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		frontShooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		frontShooterMotor.configEncoderCodesPerRev(ENCODER_COUNTS_PER_REVOLUTION);
		frontShooterMotor.setProfile(0);

		topP = 20;
    	topI = 0;
		topD = 30;
		topF = 3.8;
		
    	backShooterMotor.setP(topP);
    	backShooterMotor.setI(topI);
    	backShooterMotor.setD(topD);
    	backShooterMotor.setF(topF);

    	bottomP = 20;
    	bottomI = 0;
    	bottomD = 35;
    	bottomF = 4;
    	
    	frontShooterMotor.setP(bottomP);
    	frontShooterMotor.setI(bottomI);
    	frontShooterMotor.setD(bottomD);
    	frontShooterMotor.setF(bottomF);
    }
    
    public static ShooterSubsystem getInstance(){
		if (instance == null){
			instance = new ShooterSubsystem();
		}
		return instance;
	}
    
    public void enablePID(){
    	backShooterMotor.changeControlMode(TalonControlMode.Speed);
    	frontShooterMotor.changeControlMode(TalonControlMode.Speed);
    }
    
    public void disablePID(){
    	backShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	frontShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void setSetpoint(double backSpeed, double frontSpeed){
    	
    	backSpeed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	frontSpeed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	
    	backShooterMotor.set(backSpeed);
    	frontShooterMotor.set(frontSpeed);
    }
    
    public void setBackPower(double power){
    	backShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	backShooterMotor.set(power);
    }
    
    public void setFrontPower(double power){
    	frontShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	frontShooterMotor.set(power);
    }
    
    public double getBackRPS(){
    	return backShooterMotor.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
    }
    
    public double getFrontRPS() {
    	return frontShooterMotor.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
    }
    
    public double getBackPosition() {
    	return backShooterMotor.getPosition();
    }
    
    public double getFrontPosition() {
    	return frontShooterMotor.getPosition();
    }

	public double getBackFeetPerSecond(){
		return (getBackRPS() * SHOOTER_CIRCUMFERENCE);
	}
	
	public double getFrontFeetPerSecond(){
		return (getFrontRPS() * SHOOTER_CIRCUMFERENCE);
	} 
    
    public void initDefaultCommand() {
    }
    
    double avg_speed = 0;

	@Override
	public void updateDashboard() {
		SmartDashboard.putNumber("Back Shooter Speed", getBackRPS());
		SmartDashboard.putNumber("Back Shooter Surface Speed", getBackFeetPerSecond());
		SmartDashboard.putNumber("Back Shooter Position", getBackPosition());
		
		avg_speed = avg_speed * .9 + getFrontRPS() * .1;
		SmartDashboard.putNumber("Front Shooter Filter Speed", avg_speed);
		SmartDashboard.putNumber("Front Shooter Speed", getFrontRPS());
		SmartDashboard.putNumber("Front Shooter Surface Speed", avg_speed);
		SmartDashboard.putNumber("Front Shooter Position", getFrontPosition());
//		SmartDashboard.putNumber("Bottom Shooter Speed (ft/s)", getBottomFeetPerSecond());
	}
}

