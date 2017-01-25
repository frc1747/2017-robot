package com.frc1747.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;

import lib.frc1747.subsystems.HBRSubsystem;

/**
 *
 */
public class Drivetrain extends HBRSubsystem {

	DrivetrainSide rightSide, leftSide;
	
	public Drivetrain(){
		
		//idrk which is inverted
		rightSide = new DrivetrainSide(RobotMap.LEFT_DRIVE_MOTOR1, RobotMap.LEFT_DRIVE_MOTOR2, false, 0, 0, 0, 0, 0);
		leftSide = new DrivetrainSide(RobotMap.RIGHT_DRIVE_MOTOR1, RobotMap.RIGHT_DRIVE_MOTOR2, false, 0, 0, 0, 0, 0);
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
			motor1.set(speed);
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
}

