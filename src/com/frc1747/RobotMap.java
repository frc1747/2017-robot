package com.frc1747;

public class RobotMap {
	
	public static final int DRIVER = 0;
	public static final int OPERATOR = 1;

	public static final int LEFT_DRIVE_MOTOR1 = 11;
	public static final int LEFT_DRIVE_MOTOR2 = 12;
	public static final int RIGHT_DRIVE_MOTOR1 = 21;
	public static final int RIGHT_DRIVE_MOTOR2 = 22;
	
	public static final boolean LEFT_DRIVE_MOTOR1_INVERTED = true;
	public static final boolean LEFT_DRIVE_MOTOR2_INVERTED = true;
	public static final boolean RIGHT_DRIVE_MOTOR1_INVERTED = false; // one of the right motors needs to be inverted
	public static final boolean RIGHT_DRIVE_MOTOR2_INVERTED = true; // don't know which one it is yet
	
	public static final boolean LEFT_DRIVE_SENSOR_REVERSED = true;
	public static final boolean RIGHT_DRIVE_SENSOR_REVERSED = false;
	
	public static final int CLIMBER_MOTOR1 = 1;
	public static final int CLIMBER_MOTOR2 = 2;
	public static final boolean CLIMBER_INVERTED1 = false;
	public static final boolean CLIMBER_INVERTED2 = false;
	
	public static final int CONVEYOR_MOTOR1 = 41;
	public static final int CONVEYOR_MOTOR2 = 42;
	public static final boolean CONVEYOR_INVERTED1 = false;
	public static final boolean CONVEYOR_INVERTED2 = false;
	
	public static final int INTAKE_MOTOR = 31;
	public static final boolean INTAKE_INVERTED = false;
	
	public static final int FRONT_SHOOTER_MOTOR = 51;
	public static final int BACK_SHOOTER_MOTOR1 = 61;
	public static final int BACK_SHOOTER_MOTOR2 = 62;
	public static final boolean FRONT_SHOOTER_INVERTED = false;
	public static final boolean BACK_SHOOTER1_INVERTED = true;
	public static final boolean BACK_SHOOTER2_INVERTED = false;
	
	public static final int SHIFT_SOLENOID = 7;
	public static final int INTAKE_SOLENOID_PORT_1 = 1;
	public static final int INTAKE_SOLENOID_PORT_2 = 2;
	
	public static final int GATE_SOLENOID1_PORT_1 = 3;
	public static final int GATE_SOLENOID1_PORT_2 = 4;
	public static final int GATE_SOLENOID2_PORT_1 = 5;
	public static final int GATE_SOLENOID2_PORT_2 = 6;	
}