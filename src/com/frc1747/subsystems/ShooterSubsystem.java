package com.frc1747.subsystems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.frc1747.pid.PIDValues;
import lib.frc1747.speed_controller.HBRTalon;
import lib.frc1747.subsystems.HBRSubsystem;

public class ShooterSubsystem extends HBRSubsystem {
	
	private final double 
		SHOOTER_DIAMETER = 1.6 / 12.0, //in feet
		SHOOTER_CIRCUMFERENCE = SHOOTER_DIAMETER * Math.PI,
		ENCODER_EDGES_PER_INPUT_REVOLUTION = 12.0, // 6 cycles per 1 rev of shooter roller
		GEAR_RATIO = 2.0,
		ENCODER_EDGES_PER_OUTPUT_REVOLUTION = ENCODER_EDGES_PER_INPUT_REVOLUTION * GEAR_RATIO;
		
	private final int 
		SHOOTER_TOLERANCE = 2/*,
		SHOOTER_POWER = 0*/; //TODO: put actual value
		
	private final PIDValues backPID = new PIDValues(13, 0.01, 100, 3.29);
	private final PIDValues frontPID = new PIDValues(21, 0.03, 350, 5.7);
	//20,0.05,360 2/20/17
	//private final PIDValues backPID = new PIDValues(0, 0, 0, 5.3);

	private double frontSetpoint = 0.0, backSetpoint = 0.0, avg_speed = 0;

	private HBRTalon backShooterMotor1, frontShooterMotor;
	private CANTalon backShooterMotor2;
	
	private static ShooterSubsystem instance;
	
	private long startDebugTime = 0;
	
	PrintWriter write; 
	
    private ShooterSubsystem() {
    	
    	// Configure Back Shooter Motor 1
    	backShooterMotor1 = new HBRTalon(RobotMap.BACK_SHOOTER_MOTOR1);
    	backShooterMotor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		backShooterMotor1.reverseSensor(false);
		backShooterMotor1.configNominalOutputVoltage(+0.0f, -0.0f);
		backShooterMotor1.configPeakOutputVoltage(+12.0f, -12.0f);
		backShooterMotor1.setProfile(0);
		backShooterMotor1.setScaling(ENCODER_EDGES_PER_OUTPUT_REVOLUTION);
		backShooterMotor1.setNominalClosedLoopVoltage(12.0);
		
		// Configure Back Shooter Motor 2
    	backShooterMotor2 = new CANTalon(RobotMap.BACK_SHOOTER_MOTOR2);
		backShooterMotor2.configPeakOutputVoltage(+12.0f, -12.0f);
		backShooterMotor2.setInverted(false);
		backShooterMotor2.setNominalClosedLoopVoltage(12.0);
		
		// Configure Front Shooter Motor
    	frontShooterMotor = new HBRTalon(RobotMap.FRONT_SHOOTER_MOTOR);
    	frontShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontShooterMotor.reverseSensor(true);
		frontShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		frontShooterMotor.configPeakOutputVoltage(+0.0f, -12.0f);
		frontShooterMotor.setProfile(0);
		frontShooterMotor.setScaling(ENCODER_EDGES_PER_OUTPUT_REVOLUTION);
		frontShooterMotor.setNominalClosedLoopVoltage(12.0);

		// Set PIDF Constants for Back Shooter Motors
    	backShooterMotor1.setPIDF(backPID.P, backPID.I, backPID.D, backPID.F);
    	backShooterMotor1.setIZone(7.0);
    	//backShooterMotor1.setPID(backPID.P, backPID.I, backPID.D);
		//backShooterMotor1.setF(backPID.F);
    	// Set PIDF Constants for Front Shooter Motor
    	frontShooterMotor.setPIDF(frontPID.P, frontPID.I, frontPID.D, frontPID.F);
    	frontShooterMotor.setIZone(5.0);
		//frontShooterMotor.setPID(frontPID.P, frontPID.I, frontPID.D);
		//frontShooterMotor.setF(frontPID.F);
    	
    	try{
			File ShootValues = new File("/home/lvuser/ShooterValues.csv");
			write = new PrintWriter(new FileOutputStream(ShootValues, true));
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    public static ShooterSubsystem getInstance() {
		
    	return instance == null ? instance = new ShooterSubsystem() : instance;
	}
    
    public void enablePID() {
    	
    	backShooterMotor1.changeControlMode(TalonControlMode.Speed);
    	frontShooterMotor.changeControlMode(TalonControlMode.Speed);
    	backShooterMotor2.changeControlMode(TalonControlMode.Follower);
    	//backShooterMotor2.reverseOutput(true);
    }
    
    public void disablePID() {
    	
    	backShooterMotor1.changeControlMode(TalonControlMode.PercentVbus);
    	frontShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	//backShooterMotor2.reverseOutput(true);
    }
    
    public boolean onTarget() {
    	
    	return Math.abs(getBackRPS() - backSetpoint) < SHOOTER_TOLERANCE &&
    			Math.abs(getFrontRPS() - frontSetpoint) < SHOOTER_TOLERANCE;
    }
    
    public void setSetpoint(double backSpeed, double frontSpeed){
    	
    	frontSetpoint = frontSpeed;
    	backSetpoint = backSpeed;
    	
    	//backSpeed *= ENCODER_COUNTS_PER_REVOLUTION * ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	//frontSpeed *= ENCODER_COUNTS_PER_REVOLUTION * ENCODER_COUNTS_PER_REVOLUTION / READ_TIME;
    	
    	backShooterMotor1.set(backSpeed);
    	backShooterMotor2.set(backShooterMotor1.getDeviceID());
    	backShooterMotor2.reverseOutput(true);
    	frontShooterMotor.set(frontSpeed);
    }
    
    public void setBackPower(double power){
    	
    	backShooterMotor1.changeControlMode(TalonControlMode.PercentVbus);
    	backShooterMotor2.changeControlMode(TalonControlMode.PercentVbus);

    	backShooterMotor1.set(power);
    	backShooterMotor2.set(power);
    }
    
    public void setFrontPower(double power){
    	
    	frontShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	frontShooterMotor.set(power);
    }
    
    public double getBackRPS(){
    	return backShooterMotor1.getSpeed();
    }
    
    public double getFrontRPS() {
    	return frontShooterMotor.getSpeed();
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
    
    public void clearIAccumulation(){
    	frontShooterMotor.clearIAccum();
    	backShooterMotor1.clearIAccum();
    }

    public void writeBackValues(){
    	write.println(backShooterMotor1.getSpeed() + "," + (System.currentTimeMillis() - startDebugTime));
    }
    
	@Override
	public void updateDashboard() {
		super.updateDashboard();
	}
	
	@Override
	public void debug() {
		if (startDebugTime < System.currentTimeMillis()){
			startDebugTime = System.currentTimeMillis();
		}
		SmartDashboard.putNumber("Back Shooter Output Voltage", getBackVoltage());
		SmartDashboard.putNumber("Front Shooter Output Voltage", getFrontVoltage());

		SmartDashboard.putNumber("Back Shooter Speed", getBackRPS());
		SmartDashboard.putNumber("Back Shooter Surface Speed", getBackFeetPerSecond());
		SmartDashboard.putNumber("Back Shooter Position", getBackPosition());
		SmartDashboard.putNumber("Front Shooter Voltage", getFrontVoltage());		
		SmartDashboard.putBoolean("Back at Target", onTarget());
		avg_speed = avg_speed * .9 + getFrontRPS() * .1;
		SmartDashboard.putNumber("Front Shooter Filter Speed", avg_speed);
		SmartDashboard.putNumber("Front Shooter Speed", getFrontRPS());
		SmartDashboard.putNumber("Front Shooter Surface Speed", avg_speed);
		SmartDashboard.putNumber("Front Shooter Position", getFrontPosition());
		SmartDashboard.putNumber("Back Shooter Voltage", getBackVoltage());
//		SmartDashboard.putNumber("Bottom Shooter Speed (ft/s)", getBottomFeetPerSecond());
		SmartDashboard.putNumber("Front Shooter Position", getFrontPosition());
		SmartDashboard.putNumber("Back Shooter Position", getBackPosition());

		writeBackValues();
	}
	
	public boolean temp;
	public double sumSpeed;
	public double samples;
}
