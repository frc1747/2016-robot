package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	CANTalon leftShooterMotorOne, leftShooterMotorTwo;
	CANTalon rightShooterMotorOne, rightShooterMotorTwo;
	Solenoid led;
	double kP = .05;
	double kI = 0;
	double kD = 0;
	Counter leftCounter;
	Counter rightCounter;

	public void updatePID() {
		leftShooterMotorOne.setPID(kP, kI, kD);
		leftShooterMotorTwo.setPID(kP, kI, kD);
		rightShooterMotorOne.setPID(kP, kI, kD);
		rightShooterMotorTwo.setPID(kP, kI, kD);
	}

	public Shooter() {
		System.out.println("ShooterMotor created");
		leftShooterMotorOne = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_ONE);
		leftShooterMotorTwo = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_TWO);
		rightShooterMotorOne = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_ONE);
		rightShooterMotorTwo = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_TWO);
		leftShooterMotorOne.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
		rightShooterMotorOne.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
		leftShooterMotorOne.setInverted(true);
		leftShooterMotorTwo.setInverted(true);
		led = new Solenoid(RobotMap.LED);
		leftCounter = new Counter();
		leftCounter.setUpSource(RobotMap.LEFT_COUNTER);
		leftCounter.setUpDownCounterMode(); 
		rightCounter = new Counter();
		rightCounter.setUpSource(RobotMap.RIGHT_COUNTER);
		rightCounter.setUpDownCounterMode(); 
		SmartDashboard.putNumber("Shooter Speed", 17);
		SmartDashboard.putNumber("Shooter P", kP);
		SmartDashboard.putNumber("Shooter I", kI);
		SmartDashboard.putNumber("Shooter D", kD);
		updatePID();
	}

	// Runs the shooting motors at the speed given from teleop drive
	public void shoot(double speed) {
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
			updatePID();
		}
	}

	public void setSetpoint(double targetCurrent) {
		leftShooterMotorOne.setSetpoint(targetCurrent);
		leftShooterMotorTwo.setSetpoint(targetCurrent);
		rightShooterMotorOne.setSetpoint(targetCurrent);
		rightShooterMotorTwo.setSetpoint(targetCurrent);
	}

	public void turnOnLED() {
		led.set(true);
	}

	public void turnOffLED() {
		led.set(false);
	}

	public double getLeftSpeed() {
		return leftCounter.getPeriod();
	}

	public double getRightSpeed() {
		return rightCounter.getPeriod();
	}
}
