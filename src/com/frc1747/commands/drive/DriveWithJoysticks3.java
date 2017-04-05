package com.frc1747.commands.drive;

import com.frc1747.OI;

import lib.frc1747.controller.Logitech;
import lib.frc1747.controller.Xbox;
import lib.frc1747.subsystems.*;

import com.frc1747.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoysticks3 extends Command {
	private DriveSubsystem drive;

	// Maximum robot parameters
	private double s_v_max = 12;
	private double a_v_max = 4;

	// Command initialization
    public DriveWithJoysticks3() {
    	requires(drive = DriveSubsystem.getInstance());
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// Setup left side
    	drive.setMode(DriveSubsystem.Follower.DISTANCE, HBRSubsystem.Mode.PID);
    	drive.setPIDMode(DriveSubsystem.Follower.DISTANCE, HBRSubsystem.PIDMode.VELOCITY);
    	drive.setILimit(DriveSubsystem.Follower.DISTANCE, 0);
    	drive.setFeedforward(DriveSubsystem.Follower.DISTANCE, 0, 1 / s_v_max, 0);
    	drive.setFeedback(DriveSubsystem.Follower.DISTANCE, -0.01, 0, 0);
    	drive.resetIntegrator(DriveSubsystem.Follower.DISTANCE);
    	
    	// Setup right side
    	drive.setMode(DriveSubsystem.Follower.ANGLE, HBRSubsystem.Mode.PID);
    	drive.setPIDMode(DriveSubsystem.Follower.ANGLE, HBRSubsystem.PIDMode.VELOCITY);
    	drive.setILimit(DriveSubsystem.Follower.ANGLE, 0);
    	drive.setFeedforward(DriveSubsystem.Follower.ANGLE, 0, 0.0026/*1 / 6.71*/, 0);
    	drive.setFeedback(DriveSubsystem.Follower.ANGLE, 0.0070/*0.4*/, 0, 0);
    	drive.resetIntegrator(DriveSubsystem.Follower.ANGLE);
    	
    	// Enable the pids
    	drive.setEnabled(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Translational
    	double s_p_v = s_v_max * Math.pow(OI.getInstance().getDriver().getAxis(Logitech.LEFT_VERTICAL), 1.0);
		if(OI.getInstance().getDriver().getButton(Logitech.LT).get()) {
			s_p_v *= 0.3;
		}
		s_p_v += s_v_max * 0.2 * Math.pow(OI.getInstance().getOperator().getAxis(Xbox.LEFT_VERTICAL), 1.0);
    	drive.setSetpoint(DriveSubsystem.Follower.DISTANCE, s_p_v);
    	
    	// Rotational
    	double a_p_v = -a_v_max * Math.pow(OI.getInstance().getDriver().getAxis(Logitech.RIGHT_HORIZONTAL), 1.0);
		if(OI.getInstance().getDriver().getButton(Logitech.LT).get()) {
			a_p_v *= 0.3;
		}
		a_p_v += a_v_max * 0.2 * Math.pow(-OI.getInstance().getOperator().getAxis(Xbox.RIGHT_HORIZONTAL), 1.0);
		drive.setSetpoint(DriveSubsystem.Follower.ANGLE, a_p_v);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called when the command ends
    protected void end() {
    	drive.setEnabled(false);
    	drive.setPower(0, 0);
    }

    // Called when the command is interrupted
    protected void interrupted() {
    	end();
    }
}