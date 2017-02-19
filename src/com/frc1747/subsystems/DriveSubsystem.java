package com.frc1747.subsystems;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;
import com.frc1747.commands.drive.DriveWithJoysticks;
import com.kauailabs.navx.frc.AHRS;

import lib.frc1747.speed_controller.HBRTalon;
import lib.frc1747.subsystems.HBRSubsystem;

public class DriveSubsystem extends HBRSubsystem {

	// CONSTANTS
	private final double ENCODER_SCALING_CONSTANT = 438.5;
	
	public final boolean HIGH_GEAR = true;
	public final boolean LOW_GEAR = false;

	private final double WHEEL_DIAMETER = 4./12; //in feet
	private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; 
	//private final double ENCODER_COUNTS_PER_REVOLUTION = 360;
	private final double LEFT_KP = 0, LEFT_KI = 0, LEFT_KD = 0, LEFT_KF = 0;
	private final double RIGHT_KP = 0, RIGHT_KI = 0, RIGHT_KD = 0, RIGHT_KF = 0;
	
	private DriveSide left, right;
	private AHRS gyro;
	
	private static DriveSubsystem instance;
	
	private double oldXAccel = 0;
	private double oldYAccel = 0;
	private double oldZAccel = 0;
	PrintWriter write;
	
	private DriveSubsystem() {

		// TODO: Determine which side is inverted
		left = new DriveSide(RobotMap.LEFT_DRIVE_MOTOR1, RobotMap.LEFT_DRIVE_MOTOR2, RobotMap.LEFT_DRIVE_INVERTED, RobotMap.LEFT_DRIVE_SENSOR_REVERSED);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_MOTOR1, RobotMap.RIGHT_DRIVE_MOTOR2, RobotMap.RIGHT_DRIVE_INVERTED, RobotMap.RIGHT_DRIVE_SENSOR_REVERSED);
		left.setPIDF(LEFT_KP, LEFT_KI, LEFT_KD, LEFT_KF);
		right.setPIDF(RIGHT_KP, RIGHT_KI, RIGHT_KD, RIGHT_KF);
		gyro = new AHRS(SPI.Port.kMXP);
		
		try{
			File accelValues = new File("/home/lvuser/AccelerationValues.csv");
			write = new PrintWriter(new FileOutputStream(accelValues, true));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static DriveSubsystem getInstance() {
		return instance == null ? instance = new DriveSubsystem() : instance;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoysticks());
    }

	@Override
	public void updateDashboard() {
		super.updateDashboard();
	}
	
	@Override
	public void debug() {

		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putNumber("Gyro Rate", gyro.getRate());
		SmartDashboard.putNumber("Gyro Yaw", gyro.getYaw());
		SmartDashboard.putNumber("Gyro Roll", gyro.getRoll());
		SmartDashboard.putNumber("Gyro Pitch", gyro.getPitch());
		
		oldXAccel = 0.8 * oldXAccel + 0.2 * gyro.getRawAccelX();
		oldYAccel = 0.8 * oldYAccel + 0.2 * gyro.getRawAccelY();
		oldZAccel = 0.8 * oldZAccel + 0.2 * gyro.getRawAccelZ();
		write.println(getLeftSpeed());
		write.flush();
		SmartDashboard.putNumber("Accel X", oldXAccel);
		SmartDashboard.putNumber("Accel Y", oldYAccel);
		SmartDashboard.putNumber("Accel Z", oldZAccel);
		
		SmartDashboard.putNumber("Left Drive RPS", getLeftSpeed());
		SmartDashboard.putNumber("Right Drive RPS", getRightSpeed());
		SmartDashboard.putNumber("Right Drive Surface Speed", getRightFeetPerSecond());
		SmartDashboard.putNumber("Left Drive Surface Speed", getLeftFeetPerSecond());
		SmartDashboard.putNumber("Left Drive Position", getLeftPosition());
	}

	
	public void setPower(double leftPower, double rightPower) {
		left.setPower(leftPower);
		right.setPower(rightPower);
	}
	
	public void driveArcadeMode(double leftVert, double rightHoriz) {
		right.setPower(leftVert - rightHoriz);
		left.setPower(leftVert + rightHoriz);
	}
	
	public void enablePID() {
		right.enablePID();
		left.enablePID();
	}
	
	public void disablePID() {
		right.disablePID();
		left.disablePID();
	}
	
	public void setSetpoint(double rightSpeed, double leftSpeed) {
		right.setSetpoint(rightSpeed);
		left.setSetpoint(leftSpeed);
	}

	public double getSpeed(){
		return (right.getSpeed() + left.getSpeed()) / 2;
	}
	
	public double getForwardAcceleration(){
		//TODO: Replace axis name when possible
		return gyro.getRawAccelY();
	}
	
	public double getLateralAcceleration(){
		//TODO: Replace axis name when possible
		return gyro.getRawAccelZ();
	}
	
	public double getTurning(){
		//return Math.abs(oi.getDriver().getAxis(Logitech.RIGHT_HORIZONTAL));
		return gyro.getRate();
	}
	
	/**
	 * Determines if the robot should shift into high gear
	 * @return True if robot should be in high gear, false if it should be in low gear
	 */

	
	public void resetGyro(){
		gyro.zeroYaw();
		gyro.reset();
		gyro.resetDisplacement();
	}
	
	public double getLeftFeetPerSecond(){
		System.out.println(getLeftSpeed() + "," + WHEEL_CIRCUMFERENCE);
		return getLeftSpeed() * WHEEL_CIRCUMFERENCE /*/
				(ENCODER_COUNTS_PER_REVOLUTION * ENCODER_REFRESH_TIME)*/;
	}
	
	public double getRightFeetPerSecond(){
		return getRightSpeed() * WHEEL_CIRCUMFERENCE /*/
				(ENCODER_COUNTS_PER_REVOLUTION * ENCODER_REFRESH_TIME)*/;
	}
	
	public double getLeftSpeed(){
		return left.getSpeed();
	}
	
	public double getRightSpeed(){
		return right.getSpeed();
	}
	
	public double getRightPosition(){
		return right.getPosition();
	}
	public double getLeftPosition(){
		return left.getPosition();
	}
	
	private class DriveSide {
		
		private HBRTalon motor1, motor2;
		
		private double Kp = 0, Ki = 0, Kd = 0, Kf = 0;
		
		private DriveSide(int motorPort1, int motorPort2, boolean isInverted, boolean sensorReversed) {
			
			motor1 = new HBRTalon(motorPort1);
			motor2 = new HBRTalon(motorPort2);
			
			motor1.setInverted(isInverted);
			motor2.setInverted(isInverted);
			
			motor1.reverseSensor(sensorReversed);
			motor1.setScaling(ENCODER_SCALING_CONSTANT);
			
			//TODO: not necessarily motor1
			motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			//TODO: motor1.reverseSensor(isInverted);		
			motor1.configNominalOutputVoltage(+0.0f, -0.0f);
			//TODO: possibly +12.0f, -0.0f
			motor1.configPeakOutputVoltage(+0.0f, -12.0f);
			motor1.setProfile(0);
		}
		
		public void setPIDF(double Kp, double Ki, double Kd, double Kf) {
			motor1.setP(this.Kp = Kp);
			motor1.setI(this.Ki = Ki);
			motor1.setD(this.Kd = Kd);
			motor1.setF(this.Kf = Kf);
		}
		
		public void enablePID() {
			motor1.changeControlMode(TalonControlMode.Speed);
			motor2.changeControlMode(TalonControlMode.Follower);
		}
		
		public void disablePID() {
			motor1.changeControlMode(TalonControlMode.PercentVbus);
			motor2.changeControlMode(TalonControlMode.PercentVbus);
		}
		
		public void setPower(double power) {
			motor1.set(power);
			motor2.set(power);
		}
		
		public void setSetpoint(double speed) {
			motor1.set(speed);
		}
		
		public double getSpeed() {
			return motor1.getSpeed();
		}
		public double getPosition(){
			
			return motor1.getPosition();
			
		}
		
	}
}

