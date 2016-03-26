package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gyro implements SDLogger {

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

	@Override
	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
	}
}
