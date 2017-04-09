package com.frc1747.subsystems;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.PrintWriter;

import com.ctre.CANTalon.FeedbackDevice;
import com.frc1747.RobotMap;
import com.frc1747.commands.drive.DriveWithJoysticks;
import com.kauailabs.navx.frc.AHRS;

import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;
import lib.frc1747.speed_controller.HBRTalon;
import lib.frc1747.subsystems.HBRSubsystem;

public class DriveSubsystem extends HBRSubsystem<DriveSubsystem.Follower>{

	// CONSTANTS
	private final double LEFT_SCALING_CONSTANT = 609.6;
	private final double RIGHT_SCALING_CONSTANT = 604.1;
	
	public final boolean HIGH_GEAR = true;
	public final boolean LOW_GEAR = false;

	static final double DEADBAND = 0.1/10;
	static final double GYRO_OFFSET = 0.125;

	private DriveSide left, right;
	private AHRS gyro;
	
	private static DriveSubsystem instance;
	
	private double oldXAccel = 0;
	private double oldYAccel = 0;
	private double oldZAccel = 0;
	PrintWriter write;
	
	private Logger logger;
	
	//profile followers
	public enum Follower {
		DISTANCE, ANGLE
	}
	
	private DriveSubsystem() {
		logger = Instrumentation.getLogger("Drive Subsystem");
		
		left = new DriveSide(RobotMap.LEFT_DRIVE_MOTOR1, RobotMap.LEFT_DRIVE_MOTOR2,
				RobotMap.LEFT_DRIVE_MOTOR1_INVERTED, RobotMap.LEFT_DRIVE_MOTOR2_INVERTED, RobotMap.LEFT_DRIVE_SENSOR_REVERSED);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_MOTOR1, RobotMap.RIGHT_DRIVE_MOTOR2,
				RobotMap.RIGHT_DRIVE_MOTOR1_INVERTED, RobotMap.RIGHT_DRIVE_MOTOR2_INVERTED, RobotMap.RIGHT_DRIVE_SENSOR_REVERSED);
		left.setScaling(LEFT_SCALING_CONSTANT);
		right.setScaling(RIGHT_SCALING_CONSTANT);
		
		gyro = new AHRS(SPI.Port.kMXP);
	}
	
	public AHRS getGyro(){
		return gyro;
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

		SmartDashboard.putNumber("Gyro Rate", gyro.getRate());
		SmartDashboard.putNumber("Gyro Yaw", gyro.getYaw());
		SmartDashboard.putNumber("Gyro Roll", gyro.getRoll());
		SmartDashboard.putNumber("Gyro Pitch", gyro.getPitch());
		
		oldXAccel = 0.8 * oldXAccel + 0.2 * gyro.getRawAccelX();
		oldYAccel = 0.8 * oldYAccel + 0.2 * gyro.getRawAccelY();
		oldZAccel = 0.8 * oldZAccel + 0.2 * gyro.getRawAccelZ();
		SmartDashboard.putNumber("Accel X", oldXAccel);
		SmartDashboard.putNumber("Accel Y", oldYAccel);
		SmartDashboard.putNumber("Accel Z", oldZAccel);
		
		SmartDashboard.putNumber("Left Drive FPS", getLeftSpeed());
		SmartDashboard.putNumber("Right Drive FPS", getRightSpeed());
		SmartDashboard.putNumber("Left Drive Position", getLeftPosition());
		SmartDashboard.putNumber("Right Drive Position", getRightPosition());
		SmartDashboard.putNumber("Average Position", getAveragePosition());
		SmartDashboard.putNumber("Average speed", getAverageSpeed());
	}

	
	public void setPower(double leftPower, double rightPower) {
		left.setPower(leftPower);
		right.setPower(rightPower);
	}
	
	public void driveArcadeMode(double leftVert, double rightHoriz) {
		right.setPower(leftVert - rightHoriz);
		left.setPower((leftVert + rightHoriz) * 0.9);
	}

	public double getAverageSpeed(){
		return (right.getSpeed() + left.getSpeed()) / 2;
	}
	
	public double getForwardAcceleration(){
		return gyro.getRawAccelY();
	}
	
	public double getLateralAcceleration(){
		return gyro.getRawAccelZ();
	}
	
	public double getTurning(){
		return gyro.getRate();
	}
	
	public double getAngle() {
		return gyro.getAngle();
	}
	
	public void resetGyro(){
		gyro.zeroYaw();
		gyro.resetDisplacement();
	}
	
	public void resetEncoders() {
		left.zeroEncoder();
		right.zeroEncoder();
	}
	
	public double getLeftSpeed(){
		return -left.getSpeed();
	}
	
	public double getRightSpeed(){
		return -right.getSpeed();
	}
	
	public double getRightPosition(){
		return -right.getPosition();
	}
	public double getLeftPosition(){
		return -left.getPosition();
	}
	public double getAveragePosition() {
		return (getLeftPosition() + getRightPosition())/2;
	}
	
	private class DriveSide {
		
		private HBRTalon motor1, motor2;
		
		private DriveSide(int motorPort1, int motorPort2, boolean motor1Inverted, boolean motor2Inverted, boolean sensorReversed) {
			
			motor1 = new HBRTalon(motorPort1);
			motor2 = new HBRTalon(motorPort2);
			
			motor1.setInverted(motor1Inverted);
			motor2.setInverted(motor2Inverted);
			
			motor1.reverseSensor(sensorReversed);			
			motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		}

		public void setPower(double power) {
			motor1.set(power);
			motor2.set(power);
		}
		
		public double getSpeed() {
			return motor1.getSpeed();
		}
		public double getPosition(){
			return motor1.getPosition();
		}
		
		public void setScaling(double scaling){
			motor1.setScaling(scaling);
		}
		
		public void zeroEncoder() {
			motor1.setEncPosition(0);
		}
		
	}

	@Override
	public double[][] pidRead(){		
		double[][] output = new double[2][2];
		output[1][0] = getAverageSpeed();
		output[0][0] = getAveragePosition();
		output[0][1] = (2 * Math.PI) * ((-getGyro().getAngle()) / 360);
		output[1][1] = (2 * Math.PI) * (-getGyro().getRate() / 360);
		return output;
	}

	@Override
	public void pidWrite(double[] output) {
		driveArcadeMode(output[0], -output[1]);
	}
}

