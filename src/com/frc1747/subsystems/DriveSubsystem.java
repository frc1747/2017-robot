package com.frc1747.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.OI;
import com.frc1747.RobotMap;

import lib.frc1747.controller.Logitech;
import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class DriveSubsystem extends HBRSubsystem {

	DrivetrainSide rightSide, leftSide;
	Solenoid solenoid;
	private static DriveSubsystem instance;
	final int MULTIPLIER = 100;
	final int SHIFT_ACCELERATION_HIGH = 0;
	final int SHIFT_ACCELERATION_LOW = 0;// some number goes here.	maybe a double?? idek...
	final int SHIFT_VELOCITY_HIGH = 0;
	final int SHIFT_VELOCITY_LOW = 0;// same here
	final double TURNING_THRESHOLD_TO_SHIFT = 0;
	final boolean HIGH_GEAR = true;
	final boolean LOW_GEAR = false;
	OI oi;
	
	
	private DriveSubsystem(){
		
		//idrk which is inverted
		rightSide = new DrivetrainSide(RobotMap.LEFT_DRIVE_MOTOR1, RobotMap.LEFT_DRIVE_MOTOR2, false, 0, 0, 0, 0, 0);
		leftSide = new DrivetrainSide(RobotMap.RIGHT_DRIVE_MOTOR1, RobotMap.RIGHT_DRIVE_MOTOR2, false, 0, 0, 0, 0, 0);
		solenoid = new Solenoid(RobotMap.SHIFT_SOLENOID);
		oi = OI.getInstance();
	}
	
	public static DriveSubsystem getInstance(){
		if (instance == null){
			instance = new DriveSubsystem();
		}
		return instance;
	}
	
	private class DrivetrainSide{
		
		CANTalon motor1, motor2;
		
		private DrivetrainSide(int motorPort1, int motorPort2, boolean isInverted, double kP, double kI, double kD, double kF, int encoderCounts){
			motor1 = new CANTalon(motorPort1);
			motor2 = new CANTalon(motorPort2);
			motor1.setInverted(isInverted);
			motor2.setInverted(isInverted);
			
			//not necessarily motor1
			motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			//motor1.reverseSensor(isInverted);
			//motor1.configEncoderCodesPerRev(encoderCounts);
			motor1.configNominalOutputVoltage(+0.0f, -0.0f);
			//possibly +12.0f, -0.0f
			motor1.configPeakOutputVoltage(+0.0f, -12.0f);
			motor1.setProfile(0);
			
			motor1.setP(kP);
			motor1.setI(kI);
			motor1.setD(kD);
			motor1.setF(kF);
			
		}
		
		public void enablePID(){
			motor1.changeControlMode(TalonControlMode.Speed);
			motor2.changeControlMode(TalonControlMode.Follower);
		}
		
		public void disablePID(){
			motor1.changeControlMode(TalonControlMode.PercentVbus);
			motor2.changeControlMode(TalonControlMode.PercentVbus);
		}
		
		public void setPower(double power){
			motor1.set(power);
			motor2.set(power);
		}
		
		public void setSetpoint(double speed){
			speed = speed * MULTIPLIER;
			motor1.set(speed);
		}
		
		public int getVelocity(){
			return motor1.getEncVelocity();
		}
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

	@Override
	public void updateDashboard() {
		// TODO Auto-generated method stub
		
	}
	
	public void enablePID(){
		rightSide.enablePID();
		leftSide.enablePID();
	}
	
	public void disablePID(){
		rightSide.disablePID();
		leftSide.disablePID();
	}
	
	public void setPower(double rightPower, double leftPower){
		rightSide.setPower(rightPower);
		leftSide.setPower(leftPower);
	}
	
	public void setSetpoint(double rightSpeed, double leftSpeed){
		rightSide.setSetpoint(rightSpeed);
		leftSide.setSetpoint(leftSpeed);
	}
	
	public void toggleShift(){
		if(solenoid.get()){
			solenoid.set(false);
		}else{
			solenoid.set(true);
		}
	}
	
	public boolean isHighGear(){
		return solenoid.get() == HIGH_GEAR;
	}
	
	public boolean isLowGear(){
		return solenoid.get() == LOW_GEAR;
	}
	
	public void shiftUp(){
		solenoid.set(HIGH_GEAR);
	}
	
	public void shiftDown(){
		solenoid.set(LOW_GEAR);
	}
	
	public double getVelocity(){
		return (rightSide.getVelocity()+leftSide.getVelocity())/2;
	}
	
	public double getAcceleration(){
		return 0; //temporary, need actual acceleration
	}
	
	public double getTurning(){
		return Math.abs(oi.getDriver().getAxis(Logitech.RIGHT_HORIZONTAL));
	}
	
	//returns if in the zone to shift to high gear
	public boolean shouldShiftUp(){
		
		double slope = -SHIFT_ACCELERATION_HIGH/SHIFT_VELOCITY_LOW;
		
		if(getTurning() <= TURNING_THRESHOLD_TO_SHIFT){
			if(getAcceleration() >= slope*getVelocity() + SHIFT_ACCELERATION_HIGH){
				return HIGH_GEAR;
			}
			else{
				return LOW_GEAR;
			}
		}
		else{
			return LOW_GEAR;
		}
	}
}

