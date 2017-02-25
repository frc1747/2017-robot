package com.frc1747.commands.auton;

import com.frc1747.subsystems.DriveSubsystem;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 *
 */
public class RotateAngle extends PIDCommand {

	DriveSubsystem drive;
	double angle;
	double circleRadius;
	double turnDist;
	double startPos;
	double endPos;
	double range = 5.0;
	AHRS gyro;
	static final double P = 0;
	static final double I = 0;
	static final double D = 0;
	
    public RotateAngle(double angle) {
        super(P,I,D);
    	super.requires(drive = DriveSubsystem.getInstance());
        this.angle = angle;
        this.gyro = drive.getGyro();
         
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//drive.enablePositionPID();
    	startPos = gyro.getAngle();	    	
    	endPos = startPos + angle;
    	super.setSetpoint(endPos);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//turnDist = (angle / 360) * Math.PI * 2 * circleRadius; //angle measured clockwise from positive y
    	//drive.setSetpoint(turnDist, -turnDist);
    	/*if(angle <= 0){
    		drive.setSetpoint(5, -5);
    	}
    	else{
    		drive.setSetpoint(-5, 5);
    	}*/	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	if(Math.abs(gyro.getAngle() - endPos) <= range){
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.enableSpeedPID();
    	drive.setSetpoint(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

	@Override
	protected double returnPIDInput() {
		return endPos - gyro.getAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		drive.setSetpoint(output, -output);
	}
}
