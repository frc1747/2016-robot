package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Flashlight extends Subsystem implements SDLogger {

	private Solenoid flashlight = new Solenoid(RobotMap.FLASHLIGHT);

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
