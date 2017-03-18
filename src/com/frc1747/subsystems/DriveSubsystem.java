package com.frc1747.subsystems;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;
import com.frc1747.commands.drive.DriveWithJoysticks_OLD;
import com.frc1747.commands.drive.DriveWithJoysticks;
import com.kauailabs.navx.frc.AHRS;

import lib.frc1747.pid.PIDValues;
import lib.frc1747.speed_controller.HBRTalon;
import lib.frc1747.subsystems.HBRSubsystem;

public class DriveSubsystem extends HBRSubsystem implements PIDSource, PIDOutput{

	// CONSTANTS
	private final double LEFT_SCALING_CONSTANT = 609.6;
	private final double RIGHT_SCALING_CONSTANT = 604.1;
	
	public final boolean HIGH_GEAR = true;
	public final boolean LOW_GEAR = false;

	private final double WHEEL_DIAMETER = 4./12; //in feet
	private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; 
	//private final double ENCODER_COUNTS_PER_REVOLUTION = 360;
	//private final double LEFT_KP = 2, LEFT_KI = 0, LEFT_KD = 7, LEFT_KF = 2.3; //I = 0.01 for brake mode
	//private final double RIGHT_KP = 2, RIGHT_KI = 0, RIGHT_KD = 7, RIGHT_KF = 2.3;
	
	public static final PIDValues leftLowPIDForward = new PIDValues(3.75, 0, 100, 2.25);//previous comp P = 5
	public static final PIDValues rightLowPIDForward = new PIDValues(6.75, 0, 100, 2.245);//previous comp p = 9
	public static final PIDValues leftLowPIDBackward = new PIDValues(4.125, 0, 70, 2.13);//previous p = 5.5
	public static final PIDValues rightLowPIDBackward = new PIDValues(3.1875, 0, 30, 2.27);//previous p = 4.25
	
	public static final PIDValues leftHighPIDBackward = new PIDValues(5, 0, 50, 1.23);
	public static final PIDValues rightHighPIDBackward = new PIDValues(5, 0, 50, 1.305);
	public static final PIDValues leftHighPIDForward = new PIDValues(5.5, 0, 50, 1.24);
	public static final PIDValues rightHighPIDForward = new PIDValues(5, 0, 50, 1.26);
	
	public static final PIDValues gyroPIDValues = new PIDValues(0.02,0.001,0.04,0);
	public static final double GYRO_TOLERANCE = 0.5;
	private double gyroSetpoint = -10;
	static final double DEADBAND = 0.1/10;
	static final double GYRO_OFFSET = 0.125;
	
	private PIDController pidController;
	
	private DriveSide left, right;
	private AHRS gyro;
	
	private static DriveSubsystem instance;
	
	private double oldXAccel = 0;
	private double oldYAccel = 0;
	private double oldZAccel = 0;
	PrintWriter write;
	
	private DriveSubsystem() {
		left = new DriveSide(RobotMap.LEFT_DRIVE_MOTOR1, RobotMap.LEFT_DRIVE_MOTOR2,
				RobotMap.LEFT_DRIVE_MOTOR1_INVERTED, RobotMap.LEFT_DRIVE_MOTOR2_INVERTED, RobotMap.LEFT_DRIVE_SENSOR_REVERSED);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_MOTOR1, RobotMap.RIGHT_DRIVE_MOTOR2,
				RobotMap.RIGHT_DRIVE_MOTOR1_INVERTED, RobotMap.RIGHT_DRIVE_MOTOR2_INVERTED, RobotMap.RIGHT_DRIVE_SENSOR_REVERSED);
		left.setPIDF(leftLowPIDForward.P, leftLowPIDForward.I, leftLowPIDForward.D, leftLowPIDForward.F);
		right.setPIDF(rightLowPIDForward.P, rightLowPIDForward.I, rightLowPIDForward.D, rightLowPIDForward.F);
		gyro = new AHRS(SPI.Port.kMXP);
		left.setScaling(LEFT_SCALING_CONSTANT);
		right.setScaling(RIGHT_SCALING_CONSTANT);
		pidController = new PIDController(gyroPIDValues.P, gyroPIDValues.I, gyroPIDValues.D, gyroPIDValues.F, gyro, this);
		pidController.setAbsoluteTolerance(1);
		pidController.setOutputRange(-1, 1);
		/*try{
			File accelValues = new File("/home/lvuser/AccelerationValues.csv");
			write = new PrintWriter(new FileOutputStream(accelValues, true));
		}catch(Exception e){
			e.printStackTrace();
		}*/
		SmartDashboard.putData("Gyro PID",pidController);
	}
	
	@Override
	public void pidWrite(double output) {
		SmartDashboard.putNumber("Gyro Output Before Correction", output);
		if(Math.abs(output) > DEADBAND){
			output += GYRO_OFFSET * Math.signum(output);
		}
		driveArcadeMode(0, output);
		SmartDashboard.putNumber("Gyro PID Output", output);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		
	}
	

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return gyro.pidGet();
	}
	
	public void setGyroSetpoint(double setpoint){
		pidController.setSetpoint(setpoint);
		gyroSetpoint = setpoint;
	}
	
	public void enableGyroPID(){
		pidController.enable();
	}
	
	public void disableGyroPID(){
		pidController.disable();
	}
	
	public boolean isGyroAtTarget(){
		return Math.abs(getAngle() - gyroSetpoint) < GYRO_TOLERANCE;
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
		//write.println(getLeftSpeed());
		//write.flush();
		SmartDashboard.putNumber("Accel X", oldXAccel);
		SmartDashboard.putNumber("Accel Y", oldYAccel);
		SmartDashboard.putNumber("Accel Z", oldZAccel);
		
		SmartDashboard.putNumber("Left Drive FPS", getLeftSpeed());
		SmartDashboard.putNumber("Right Drive FPS", getRightSpeed());
		//System.out.println("left " + getLeftSpeed());
		//System.out.println("right " + getRightSpeed());
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
		left.setPower(leftVert + rightHoriz);
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
	
	public void setLeftPIDF(PIDValues values){
		left.setPIDF(values.P, values.I, values.D, values.F);
	}
	
	public void setRightPIDF(PIDValues values){
		right.setPIDF(values.P, values.I, values.D, values.F);
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
		
		private double Kp = 0, Ki = 0, Kd = 0, Kf = 0;
		
		private DriveSide(int motorPort1, int motorPort2, boolean motor1Inverted, boolean motor2Inverted, boolean sensorReversed) {
			
			motor1 = new HBRTalon(motorPort1);
			motor2 = new HBRTalon(motorPort2);
			
			motor1.setInverted(motor1Inverted);
			motor2.setInverted(motor2Inverted);
			
			motor1.reverseSensor(sensorReversed);			
			motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		}

		public void setPIDF(double Kp, double Ki, double Kd, double Kf) {
			motor1.setP(this.Kp = Kp);
			motor1.setI(this.Ki = Ki);
			motor1.setD(this.Kd = Kd);
			motor1.setF(this.Kf = Kf);
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
	public double[] pidRead() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pidWrite(double[] output) {
		// TODO Auto-generated method stub
		
	}
}

