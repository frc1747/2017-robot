package com.frc1747.subsystems;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;
import com.frc1747.commands.drive.DriveWithJoysticks;
import com.kauailabs.navx.frc.AHRS;
import lib.frc1747.subsystems.HBRSubsystem;

public class DriveSubsystem extends HBRSubsystem {

	// CONSTANTS
	private final int MULTIPLIER = 100;
	private final int SHIFT_ACCELERATION_HIGH = 1;
	private final int SHIFT_ACCELERATION_LOW = 1;// some number goes here.	maybe a double?? idek...
	private final int SHIFT_VELOCITY_HIGH = 1;
	private final int SHIFT_VELOCITY_LOW = 1;// same here
	
	public final boolean HIGH_GEAR = true;
	public final boolean LOW_GEAR = false;
	
	private final double TURNING_THRESHOLD_TO_SHIFT = 0;
	private final double WHEEL_DIAMETER = 4/12; //in feet
	private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; 
	private final double ENCODER_COUNTS_PER_REVOLUTION = 4;
	private final double ENCODER_REFRESH_TIME = .1; //in seconds, motor speed is recorded over intervals of this
	private final double LEFT_KP = 0, LEFT_KI = 0, LEFT_KD = 0, LEFT_KF = 0;
	private final double RIGHT_KP = 0, RIGHT_KI = 0, RIGHT_KD = 0, RIGHT_KF = 0;
	
	private DriveSide left, right;
	private AHRS gyro;
	
	private static DriveSubsystem instance;
	
	private DriveSubsystem() {

		// TODO: Determine which side is inverted
		left = new DriveSide(RobotMap.LEFT_DRIVE_MOTOR1, RobotMap.LEFT_DRIVE_MOTOR2, RobotMap.LEFT_DRIVE_INVERTED);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_MOTOR1, RobotMap.RIGHT_DRIVE_MOTOR2, RobotMap.RIGHT_DRIVE_INVERTED);
		left.setPIDF(LEFT_KP, LEFT_KI, LEFT_KD, LEFT_KF);
		right.setPIDF(RIGHT_KP, RIGHT_KI, RIGHT_KD, RIGHT_KF);
		gyro = new AHRS(SPI.Port.kMXP);
	}
	
	public static DriveSubsystem getInstance() {
		return instance == null ? instance = new DriveSubsystem() : instance;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoysticks());
    }

	@Override
	public void updateDashboard() {
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putNumber("Gyro Rate", gyro.getRate());
		
		SmartDashboard.putNumber("Right Velocity (ft/s)", getRightFeetPerSecond());
		SmartDashboard.putNumber("Left Velocity (ft/s)", getLeftFeetPerSecond());
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

	public double getVelocity(){
		return (right.getVelocity()+left.getVelocity())/2;
	}
	
	public double getForwardAcceleration(){
		//TODO: Replace axis name when possible
		return gyro.getRawAccelX();
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
	public boolean shouldShiftUp(){
		
		double slope = -SHIFT_ACCELERATION_HIGH/SHIFT_VELOCITY_LOW;
		
		if(Math.abs(getTurning()) <= TURNING_THRESHOLD_TO_SHIFT) {
			//TODO: Might need to be XAcceleration/YAcceleration/Combination of the two
			if(getForwardAcceleration() >= slope*getVelocity() + SHIFT_ACCELERATION_HIGH) {
				return HIGH_GEAR;
			} else {
				return LOW_GEAR;
			}
		} else {
			return LOW_GEAR;
		}
	}
	
	public void resetGyro(){
		gyro.zeroYaw();
		gyro.reset();
		gyro.resetDisplacement();
	}
	
	public double getLeftFeetPerSecond(){
		return getLeftVelocity() * WHEEL_CIRCUMFERENCE /
				(ENCODER_COUNTS_PER_REVOLUTION * ENCODER_REFRESH_TIME);
	}
	
	public double getRightFeetPerSecond(){
		return getRightVelocity() * WHEEL_CIRCUMFERENCE /
				(ENCODER_COUNTS_PER_REVOLUTION * ENCODER_REFRESH_TIME);
	}
	
	public double getLeftVelocity(){
		return left.getVelocity();
	}
	
	public double getRightVelocity(){
		return right.getVelocity();
	}
	
	private class DriveSide {
		
		private CANTalon motor1, motor2;
		
		double Kp = 0, Ki = 0, Kd = 0, Kf = 0;
		
		private DriveSide(int motorPort1, int motorPort2, boolean isInverted) {
			
			motor1 = new CANTalon(motorPort1);
			motor2 = new CANTalon(motorPort2);
			
			motor1.setInverted(isInverted);
			motor2.setInverted(isInverted);
			
			//TODO: not necessarily motor1
			motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			//TODO: motor1.reverseSensor(isInverted);
			//TODO: motor1.configEncoderCodesPerRev(encoderCounts);
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
		
		public void setEncoderCount(double encCount) {
			// TODO: Add Encoder Count Implementation
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
			motor1.set(speed * MULTIPLIER);
		}
		
		public int getVelocity() {
			return motor1.getEncVelocity();
		}
		
	}
}

