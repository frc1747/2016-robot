package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends PIDSubsystem {

	CANTalon leftShooterMotorOne, leftShooterMotorTwo;
	CANTalon rightShooterMotorOne, rightShooterMotorTwo;
	Solenoid led;

	public Shooter(double P, double I, double D) {
		super(P, I, D);
		System.out.println("ShooterMotor created");
		leftShooterMotorOne = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_ONE);
		leftShooterMotorTwo = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_TWO);
		rightShooterMotorOne = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_ONE);
		rightShooterMotorTwo = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_TWO);
		// leftShooterMotorOne.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
		// rightShooterMotorOne.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
		leftShooterMotorOne.setInverted(true);
		leftShooterMotorTwo.setInverted(true);
		led = new Solenoid(RobotMap.LED);
		SmartDashboard.putNumber("Shooter Speed", 0.5);
	}

	// Runs the shooting motors at the speed given from teleop drive
	public void shoot(double speed) {
		System.out.println("Shooting " + speed);
		leftShooterMotorOne.set(speed);
		leftShooterMotorTwo.set(speed);
		rightShooterMotorOne.set(speed);
		rightShooterMotorTwo.set(speed);
	}

	@Override
	protected void initDefaultCommand() {
	}

	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Left Shooter Speed", getLeftSpeed());
		SmartDashboard.putNumber("Right Shooter Speed", getRightSpeed());
	}

	@Override
	protected double returnPIDInput() {
		return 0;
	}

	@Override
	protected void usePIDOutput(double arg0) {
		// shoot(arg0);
	}

	public void turnOnLED() {
		led.set(true);
	}

	public void turnOffLED() {
		led.set(false);
	}

	public double getLeftSpeed() {
		return leftShooterMotorOne.getSpeed();
	}

	public double getRightSpeed() {
		return rightShooterMotorOne.getSpeed();
	}
}
