package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Flashlight extends Subsystem implements SDLogger {

	private Solenoid flashlight;

	public Flashlight() {
		flashlight = new Solenoid(RobotMap.FLASHLIGHT);
	}

	public void turnOnFlashlight() {
		flashlight.set(true);
	}

	public void turnOffFlashlight() {
		flashlight.set(false);
	}

	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public void logToSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
}
