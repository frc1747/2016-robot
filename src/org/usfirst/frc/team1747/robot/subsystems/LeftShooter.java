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
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LeftShooter extends Subsystem implements SDLogger, PIDSource, PIDOutput {

	private CANTalon motorOne = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_ONE);
	private CANTalon motorTwo = new CANTalon(RobotMap.LEFT_SHOOTER_MOTOR_TWO);
	private Counter counter;
	private PIDController controller;
	private boolean pidEnabled = false;
	private boolean atTarget = false;
	private int count = 0;

	private static final double KP = 0.0114, KI = 0.300, KD = 0.075, KF = 0;
	private static final double targetShooterSpeed = 65;
	private static final double shooterErrorMargin = 1;

	public LeftShooter() {
		//motorOne.changeControlMode(TalonControlMode.Voltage);
		//motorTwo.changeControlMode(TalonControlMode.Voltage);
		motorOne.setInverted(RobotMap.LEFT_SHOOTER_INVERTED);
		motorTwo.setInverted(RobotMap.LEFT_SHOOTER_INVERTED);
		counter = new Counter();
		counter.setUpSource(RobotMap.LEFT_COUNTER);
		counter.setUpDownCounterMode();
		counter.setPIDSourceType(PIDSourceType.kRate);
		counter.setDistancePerPulse(1.0);
		counter.setSamplesToAverage(3);
		
		controller = new PIDController(KP, KI, KD, KF, this, this);
		controller.setAbsoluteTolerance(1.0);
		controller.setOutputRange(0, 1);
		
		SmartDashboard.putData("Left Shooter PID", controller);
	}

	public double getTargetShooterSpeed() {
		return targetShooterSpeed;
	}

	@Override
	protected void initDefaultCommand() {
	}

	public void setSpeed(double output) {
		motorOne.set(output);
		motorTwo.set(output);
	}
	
	public double getSpeed() {
		return counter.getRate();
	}

	public double getVoltage() {
		return motorOne.getOutputVoltage();
	}
	
	public void pidEnable() {
		count = 0;
		controller.enable();
		pidEnabled = true;
	}
	
	public void pidDisable() {
		count = 0;
		controller.disable();
		pidEnabled = false;
	}
	
	public boolean isPidEnabled() {
		return pidEnabled;
	}
	
	public boolean isAtTarget() {
		if(Math.abs(targetShooterSpeed - getSpeed()) < shooterErrorMargin) {
			count++;
		}
		else {
			count = 0;
			atTarget = false;
		}
		if(count > 5) atTarget = true;
		SmartDashboard.putNumber("LEFT SHOOTER COUNT", count);
		SmartDashboard.putBoolean("LEFT IS AT TARGET", atTarget);

		return atTarget;
	}
	
	public void setSetpoint(double output) {
		controller.setSetpoint(output);
	}
	
	@Override
	public void pidWrite(double output) {
		if (getSpeed() < 20) {
			output = Math.min(0.7, output);
		}

		motorOne.set(output);
		motorTwo.set(output);
		SmartDashboard.putNumber("Left Shooter Power", output);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kRate;
	}

	@Override
	public double pidGet() {
		return counter.getRate();
	}

	@Override
	public void logToSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
}
