package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;
import org.usfirst.frc.team1747.robot.commands.IntakeManual;

public class Intake extends Subsystem implements SDLogger, PIDSource, PIDOutput {

	private Talon leftLiftMotor;
	private Talon rightLiftMotor;
	private Talon rollerMotor;
	private DigitalInput ballIntake;
	private Encoder encoder;
	private PIDController controller;
	private static final double KP = 0.05, KI = 0, KD = 0, KF = 0;
	
	private double intakeSetpoint;
	
	public Intake() {
		leftLiftMotor = new Talon(RobotMap.LEFT_LIFT_MOTOR);
		rightLiftMotor = new Talon(RobotMap.RIGHT_LIFT_MOTOR);
		rollerMotor = new Talon(RobotMap.ROLLER_MINICIM);
		ballIntake = new DigitalInput(RobotMap.BALL_INTAKE);
		
		controller = new PIDController(KP, KI, KD, KF, this, this);
		controller.setOutputRange(-0.5, 0.25);
		
		encoder = new Encoder(RobotMap.INTAKE_ENCODER_A, RobotMap.INTAKE_ENCODER_B);
		encoder.setDistancePerPulse(1.0);
	}

	// Moves the arm
	public void liftControl(double output) {
		leftLiftMotor.set(output);
		rightLiftMotor.set(output);
	}
	
	public double getPower() {
		return leftLiftMotor.get();
	}

	public void moveLiftDown() {
		liftControl(-.25);
	}

	public void moveLiftUp() {
		liftControl(.5);
	}

	public void liftStop() {
		liftControl(0);
	}

	public void intakeBall() {
		rollerControl(1);
	}

	public void ejectBall() {
		rollerControl(-1);
	}

	public void rollerStop() {
		rollerMotor.set(0);
	}

	// Sets the pickup speed
	public void rollerControl(double speed) {
		rollerMotor.set(speed);
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new IntakeManual());
	}

	public boolean hasBall() {
		return !ballIntake.get();
	}

	public void logToSmartDashboard() {
		SmartDashboard.putBoolean("BallIntake", hasBall());
		SmartDashboard.putNumber("Intake Lift Distance", getDistance());
		SmartDashboard.putNumber("Intake Motor Output", getPower());
		SmartDashboard.putData("Intake PID Controller", controller);
	}

	// returns encoder distance
	private double getDistance() {
		return encoder.getDistance();
	}

	// returns true if at target, false if not at target
	public boolean isAtTarget() {
		return Math.abs(intakeSetpoint - getDistance()) < 5;
	}

	@Override
	public void pidWrite(double output) {
		output = -output;
		rightLiftMotor.set(output);
		leftLiftMotor.set(output);
		SmartDashboard.putNumber("Intake PID Output", output);
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
		return encoder.getDistance();
	}

	public void setSetpoint(double setpoint) {
		intakeSetpoint = setpoint;
		controller.setSetpoint(setpoint);
	}
	
	public void resetEncoder() {
		encoder.reset();
	}
	
	public void pidEnable() {
		controller.enable();
	}
	
	public void pidDisable() {
		controller.disable();
	}
}