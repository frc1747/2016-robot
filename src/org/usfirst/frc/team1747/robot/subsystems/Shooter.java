package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	CANTalon leftShooterMotorOne, leftShooterMotorTwo;
	CANTalon rightShooterMotorOne, rightShooterMotorTwo;
	Solenoid led;
	double kP;
	double kI;
	double kD;

	public void setupPID(CANTalon talon) {
		talon.setControlMode(TalonControlMode.Current.getValue());
		talon.setPID(kP, kI, kD);
	}

	public void updatePID(CANTalon talon) {
		talon.setPID(kP, kI, kD);
	}

	public Shooter() {
		System.out.println("ShooterMotor created");
		leftShooterMotorOne = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_ONE);
		leftShooterMotorTwo = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_TWO);
		rightShooterMotorOne = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_ONE);
		rightShooterMotorTwo = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_TWO);
		leftShooterMotorOne.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rightShooterMotorOne.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		leftShooterMotorOne.setInverted(true);
		leftShooterMotorTwo.setInverted(true);
		led = new Solenoid(RobotMap.LED);
		SmartDashboard.putNumber("Shooter Speed", 0.5);
		SmartDashboard.putNumber("Shooter P", kP);
		SmartDashboard.putNumber("Shooter I", kI);
		SmartDashboard.putNumber("Shooter D", kD);
		setupPID(leftShooterMotorOne);
		setupPID(leftShooterMotorTwo);
		setupPID(rightShooterMotorOne);
		setupPID(rightShooterMotorTwo);
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
		double previouskP = kP;
		double previouskI = kI;
		double previouskD = kD;

		kP = SmartDashboard.getNumber("Shooter P", kP);
		kI = SmartDashboard.getNumber("Shooter I", kI);
		kD = SmartDashboard.getNumber("Shooter D", kD);

		if (previouskP != kP || previouskI != kI || previouskD != kD) {
			updatePID(leftShooterMotorOne);
			updatePID(leftShooterMotorTwo);
			updatePID(rightShooterMotorOne);
			updatePID(rightShooterMotorTwo);
		}
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
