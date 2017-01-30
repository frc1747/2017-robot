package com.frc1747.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.frc1747.RobotMap;

import lib.frc1747.subsystems.HBRSubsystem;



public class ShooterSubsystem extends HBRSubsystem {

	CANTalon topShooterMotor, bottomShooterMotor;
	double topP, topI, topD, topF;
	double bottomP, bottomI, bottomD, bottomF;
	private static ShooterSubsystem instance;
	
    private ShooterSubsystem(){
    	topShooterMotor = new CANTalon(RobotMap.TOP_SHOOTER_MOTOR);
    	bottomShooterMotor = new CANTalon(RobotMap.BOTTOM_SHOOTER_MOTOR);
    	
		
		topShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//TODO: topShooterMotor.reverseSensor();
		topShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		//TODO: possibly +12.0f, -0.0f
		topShooterMotor.configPeakOutputVoltage(+0.0f, -12.0f);
		topShooterMotor.setProfile(0);
		
		bottomShooterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//TODO: bottomShooterMotor.reverseSensor();
		bottomShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		//TODO: possibly +12.0f, -0.0f
		bottomShooterMotor.configPeakOutputVoltage(+0.0f, -12.0f);
		bottomShooterMotor.setProfile(0);
    	
    	topShooterMotor.setP(topP);
    	topShooterMotor.setI(topI);
    	topShooterMotor.setD(topD);
    	topShooterMotor.setF(topF);
    	
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
    
    public void shootTop(double speed){
    	topShooterMotor.set(speed);
    }
    
    public void shootBottom(double speed){
    	bottomShooterMotor.set(speed);
    }
    
    public void setTopPower(double power){
    	topShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	topShooterMotor.set(power);
    }
    
    public void setBottomPower(double power){
    	bottomShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
    	bottomShooterMotor.set(power);
    }
    
    
	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    
    }

	@Override
	public void updateDashboard() {
		// Make update smart dashboard calls here.
	}
}

