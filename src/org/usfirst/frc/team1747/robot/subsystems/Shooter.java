package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	CANTalon leftShooterMotorOne, leftShooterMotorTwo;
	CANTalon rightShooterMotorOne, rightShooterMotorTwo;

	public Shooter() {
		System.out.println("ShooterMotor created");
		leftShooterMotorOne = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_ONE);
		leftShooterMotorTwo = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_TWO);
		rightShooterMotorOne = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_ONE);
		rightShooterMotorTwo = new CANTalon(RobotMap.RIGHT_SHOOTER_MOTOR_TWO);
		rightShooterMotorOne.setInverted(true);
		rightShooterMotorTwo.setInverted(true);
		SmartDashboard.putNumber("Shooter Speed", 1.0);
	}

	public void shoot(double speed) {
		System.out.println("Shooting " + speed);
		leftShooterMotorOne.set(speed);
		leftShooterMotorTwo.set(speed);
		rightShooterMotorOne.set(speed);
		rightShooterMotorTwo.set(speed);
	}

	protected void initDefaultCommand() {
	}

	public void logToSmartDashboard() {
	}

}
