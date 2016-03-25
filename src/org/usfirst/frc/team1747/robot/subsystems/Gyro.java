package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogGyro;

public class Gyro {

	private AnalogGyro gyro;

	public Gyro() {
		gyro = new AnalogGyro(RobotMap.GYRO);
	}

	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public void resetGyro() {
		gyro.reset();
	}
}
