package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {

	CANTalon leftMotorOne, leftMotorTwo;
	CANTalon rightMotorOne, rightMotorTwo;

	public Shooter() {
		System.out.println("Motor created");
		leftMotorOne = new CANTalon(1);
		leftMotorTwo = new CANTalon(22);
		rightMotorOne = new CANTalon(22);
		rightMotorTwo = new CANTalon(2);
	}

	public void shoot(double speed) {
		System.out.println("Shooting " + speed);
		leftMotorOne.set(-speed);
		leftMotorTwo.set(-speed);
		rightMotorOne.set(speed);
		rightMotorTwo.set(speed);
	}

	protected void initDefaultCommand() {
	}

}
