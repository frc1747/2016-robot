package org.usfirst.frc.team1747.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static final String INTAKE_CAMERA = "Cam1";
	public static final String SHOOTER_CAMERA = "Cam2";

	public static final int LEFT_DRIVE_CIM_ONE = 21;
	public static final int LEFT_DRIVE_CIM_TWO = 22;
	public static final int LEFT_DRIVE_MINICIM = 23;
	public static final int RIGHT_DRIVE_CIM_ONE = 11;
	public static final int RIGHT_DRIVE_CIM_TWO = 12;
	public static final int RIGHT_DRIVE_MINICIM = 13;

	public static final int LEFT_LIFT_MOTOR = 31;
	public static final int RIGHT_LIFT_MOTOR = 32;
	public static final int ROLLER_MINICIM = 0;

	public static final int LEFT_SHOOTER_MOTOR_ONE = 41;
	public static final int LEFT_SHOOTER_MOTOR_TWO = 42;
	public static final int RIGHT_SHOOTER_MOTOR_ONE = 43;
	public static final int RIGHT_SHOOTER_MOTOR_TWO = 44;

	public static final int BALL_INTAKE = 0;
	public static final int RIGHT_CLIMB_CIM = 0;
	public static final int LEFT_CLIMB_CIM = 0;

	public static final int LEFT_COUNTER = 3;
	public static final int RIGHT_COUNTER = 1;
	
	public static final int LED = 0;
}
