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
	private final int SHOOTER_TOLERANCE = 10;

	CANTalon backShooterMotor1, backShooterMotor2, frontShooterMotor;
	double backP, backI, backD, backF;
	double frontP, frontI, frontD, frontF;
	private static ShooterSubsystem instance;
	private double frontSetpoint = 0.0;
	private double backSetpoint = 0.0;
	
	public final double SHOOTER_POWER = 0; //TODO: put actual value
	
    private ShooterSubsystem(){
    	backShooterMotor1 = new CANTalon(RobotMap.BACK_SHOOTER_MOTOR1);
    	backShooterMotor2 = new CANTalon(RobotMap.BACK_SHOOTER_MOTOR2);
    	frontShooterMotor = new CANTalon(RobotMap.FRONT_SHOOTER_MOTOR);
    	
		backShooterMotor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		backShooterMotor1.reverseSensor(true);
		backShooterMotor1.configNominalOutputVoltage(+0.0f, -0.0f);
		backShooterMotor1.configPeakOutputVoltage(+12.0f, -12.0f);
		backShooterMotor1.configEncoderCodesPerRev(ENCODER_COUNTS_PER_REVOLUTION);
		backShooterMotor1.setProfile(0);
		backShooterMotor2.setInverted(true);
		
		frontShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontShooterMotor.reverseSensor(true);
		frontShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		frontShooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		frontShooterMotor.configEncoderCodesPerRev(ENCODER_COUNTS_PER_REVOLUTION);
		frontShooterMotor.setProfile(0);

		backP = 10;
    	backI = 0;
		backD = 70;
		backF = 3.3;
		
    	backShooterMotor1.setP(backP);
    	backShooterMotor1.setI(backI);
    	backShooterMotor1.setD(backD);
    	backShooterMotor1.setF(backF);

    	frontP = 8;
    	frontI = 0;
    	frontD = 70;
    	frontF = 5.3;
    	
    	frontShooterMotor.setP(frontP);
    	frontShooterMotor.setI(frontI);
    	frontShooterMotor.setD(frontD);
    	frontShooterMotor.setF(frontF);
    }
    
    public static ShooterSubsystem getInstance(){
		if (instance == null){
			instance = new ShooterSubsystem();
		}
		return instance;
	}
    
    public void enablePID(){
    	backShooterMotor1.changeControlMode(TalonControlMode.Speed);
    	frontShooterMotor.changeControlMode(TalonControlMode.Speed);
    	backShooterMotor2.changeControlMode(TalonControlMode.Follower);
    	backShooterMotor2.reverseOutput(true);
    }
    
    public void disablePID(){
    	backShooterMotor1.changeControlMode(TalonControlMode.PercentVbus);
    	frontShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	backShooterMotor2.reverseOutput(true);
    }
    
    public boolean isAtTarget() {
    	return Math.abs(getBackRPS() - backSetpoint) < SHOOTER_TOLERANCE;
    }
    
    public void setSetpoint(double backSpeed, double frontSpeed){
    	frontSetpoint = frontSpeed;
    	backSetpoint = backSpeed;
    	backSpeed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	frontSpeed *= ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	
    	backShooterMotor1.set(backSpeed);
    	backShooterMotor2.set(backShooterMotor1.getDeviceID());
    	frontShooterMotor.set(frontSpeed);
    }
    
    public void setBackPower(double power){
    	backShooterMotor1.changeControlMode(TalonControlMode.PercentVbus);
    	backShooterMotor1.set(power);
    	backShooterMotor2.changeControlMode(TalonControlMode.PercentVbus);
    	backShooterMotor2.set(power);
    }
    
    public void setFrontPower(double power){
    	frontShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	frontShooterMotor.set(power);
    }
    
    public double getBackRPS(){
    	return backShooterMotor1.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
    }
    
    public double getFrontRPS() {
    	return frontShooterMotor.getSpeed() / (ENCODER_COUNTS_PER_REVOLUTION / READ_TIME);
    }
    
    public double getBackPosition() {
    	return backShooterMotor1.getPosition();
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
    
    public double getFrontVoltage() {
    	return frontShooterMotor.getOutputVoltage();
    }
    
    public double getBackVoltage() {
    	return backShooterMotor1.getOutputVoltage();
    }
    
    double avg_speed = 0;

	@Override
	public void updateDashboard() {
		SmartDashboard.putNumber("Back Shooter Speed", getBackRPS());
		SmartDashboard.putNumber("Back Shooter Surface Speed", getBackFeetPerSecond());
		SmartDashboard.putNumber("Back Shooter Position", getBackPosition());
		SmartDashboard.putNumber("Front Shooter Voltage", getFrontVoltage());		
		SmartDashboard.putBoolean("Back at Target", isAtTarget());
		avg_speed = avg_speed * .9 + getFrontRPS() * .1;
		SmartDashboard.putNumber("Front Shooter Filter Speed", avg_speed);
		SmartDashboard.putNumber("Front Shooter Speed", getFrontRPS());
		SmartDashboard.putNumber("Front Shooter Surface Speed", avg_speed);
		SmartDashboard.putNumber("Front Shooter Position", getFrontPosition());
		SmartDashboard.putNumber("Back Shooter Voltage", getBackVoltage());
//		SmartDashboard.putNumber("Bottom Shooter Speed (ft/s)", getBottomFeetPerSecond());
	}
}
