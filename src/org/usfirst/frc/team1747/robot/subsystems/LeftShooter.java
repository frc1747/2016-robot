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

	private static final double KP = 0, KI = 0, KD = 0, KF = 0;
	private static final double targetShooterSpeed = 70;
	private static final double shooterErrorMargin = 0.020;

	public LeftShooter() {
		motorOne.changeControlMode(TalonControlMode.Voltage);
		motorTwo.changeControlMode(TalonControlMode.Voltage);
		motorOne.setInverted(RobotMap.LEFT_SHOOTER_INVERTED);
		motorTwo.setInverted(RobotMap.LEFT_SHOOTER_INVERTED);
		counter = new Counter();
		counter.setUpSource(RobotMap.LEFT_COUNTER);
		counter.setUpDownCounterMode();
		counter.setPIDSourceType(PIDSourceType.kRate);
		counter.setDistancePerPulse(1.0);
		counter.setSamplesToAverage(1);
		
		controller = new PIDController(KP, KI, KD, KF, this, this);
		controller.setAbsoluteTolerance(1.0);
		controller.setOutputRange(0, 12);
		
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
		System.out.println("Setting speed to " + output);
	}
	
	public double getSpeed() {
		return counter.getRate();
	}

	public void pidEnable() {
		controller.enable();
		pidEnabled = true;
	}
	
	public void pidDisable() {
		controller.disable();
		pidEnabled = false;
	}
	
	public boolean isPidEnabled() {
		return pidEnabled;
	}
	
	public boolean isAtTarget() {
		if(Math.abs(targetShooterSpeed - this.getSpeed()) < shooterErrorMargin) {
			count++;
		}
		else {
			count = 0;
			atTarget = false;
		}
		if(count > 20) atTarget = true;
		
		return atTarget;
	}
	
	public void setSetpoint(double output) {
		controller.setSetpoint(output);
	}
	
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		motorOne.set(output);
		motorTwo.set(output);
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
		// TODO Auto-generated method stub
		return counter.getRate();
	}

	@Override
	public void logToSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
}
