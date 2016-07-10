package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem implements SDLogger {

	private boolean pidEnabled = false;
	private ShooterSide leftShooter, rightShooter;

	private static final double LKP = 0.0126, LKI = 0.300, LKD = 0.075, LKF = 0;
	private static final double RKP = 0.012, RKI = 0.300, RKD = 0.075, RKF = 0;

	private static final double targetShooterSpeed = 67;

	public Shooter() {
		leftShooter = new ShooterSide(RobotMap.LEFT_SHOOTER_MOTOR_ONE, RobotMap.LEFT_SHOOTER_MOTOR_TWO,
				RobotMap.LEFT_SHOOTER_INVERTED, RobotMap.LEFT_COUNTER, LKP, LKI, LKD, LKF, "LEFT");

		rightShooter = new ShooterSide(RobotMap.RIGHT_SHOOTER_MOTOR_ONE, RobotMap.RIGHT_SHOOTER_MOTOR_TWO,
				RobotMap.RIGHT_SHOOTER_INVERTED, RobotMap.RIGHT_COUNTER, RKP, RKI, RKD, RKF, "RIGHT");
	}

	@Override
	protected void initDefaultCommand() {
	}

	public double getTargetShooterSpeed() {
		return targetShooterSpeed;
	}

	public void setSpeed(double output) {
		leftShooter.setSpeed(output);
		rightShooter.setSpeed(output);
	}
	
	public double getLeftSpeed() {
		return leftShooter.getSpeed();
	}
	
	public double getRightSpeed() {
		return rightShooter.getSpeed();
	}
	
	public double getLeftVoltage() {
		return leftShooter.getVoltage();
	}
	
	public double getRightVoltage() {
		return rightShooter.getVoltage();
	}
	
	public void pidEnable() {
		pidEnabled = true;
		leftShooter.pidEnable();
		rightShooter.pidEnable();
	}
	
	public void pidDisable() {
		pidEnabled = false;
		leftShooter.pidDisable();
		rightShooter.pidDisable();
	}
	
	public boolean isPidEnabled() {
		return pidEnabled;
	}
	
	public boolean isAtTarget() {
		return (leftShooter.isAtTarget() && rightShooter.isAtTarget());
	}
	
	public void setSetpoint(double targetShooterSpeed) {
		leftShooter.setSetpoint(targetShooterSpeed);
		rightShooter.setSetpoint(targetShooterSpeed);
	}

	@Override
	public void logToSmartDashboard() {
	}
	
	private class ShooterSide implements PIDSource, PIDOutput {
		private CANTalon motorOne;
		private CANTalon motorTwo;
		private Counter counter;
		private PIDController controller;
		private boolean atTarget = false;
		private double currentSpeed;
		private double previousSpeed = 0.0;
		private int count = 0;
		String side;

		private static final double shooterErrorMargin = 1.0;

		public ShooterSide(int motorOneId, int motorTwoId, boolean motorsInverted, int counterId,
				double KP, double KI, double KD, double KF, String shooterSide) {
			motorOne = new CANTalon(motorOneId);
			motorTwo = new CANTalon(motorTwoId);
			motorOne.setInverted(motorsInverted);
			motorTwo.setInverted(motorsInverted);

			counter = new Counter();
			counter.setUpSource(counterId);
			counter.setUpDownCounterMode();
			counter.setPIDSourceType(PIDSourceType.kRate);
			counter.setDistancePerPulse(1.0);
			counter.setSamplesToAverage(1);
			
			controller = new PIDController(KP, KI, KD, KF, this, this);
			controller.setAbsoluteTolerance(1.0);
			controller.setOutputRange(0, 1);
			
			side = shooterSide;
			
			SmartDashboard.putData(side + " SHOOTER PID", controller);
		}

		public void setSpeed(double output) {
			motorOne.set(output);
			motorTwo.set(output);
		}
		
		public double getSpeed() {
			currentSpeed = counter.getRate();
			if(Math.abs(currentSpeed - previousSpeed) < 20 && previousSpeed != 0.0) {
				currentSpeed = previousSpeed;
			}
			previousSpeed = currentSpeed;
			return currentSpeed;
		}
		
		public double getVoltage() {
			return motorOne.getOutputVoltage();
		}

		public void pidEnable() {
			count = 0;
			previousSpeed = 0.0;
			controller.enable();
		}
		
		public void pidDisable() {
			count = 0;
			previousSpeed = 0.0;
			controller.disable();
		}
		
		public boolean isAtTarget() {
			if(Math.abs(targetShooterSpeed - getSpeed()) < shooterErrorMargin) {
				count++;
			}
			else {
				count = 0;
				atTarget = false;
			}
			if(count > 7) atTarget = true;
			SmartDashboard.putNumber(side + " SHOOTER COUNT", count);
			SmartDashboard.putBoolean(side + " IS AT TARGET", atTarget);
			
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
			SmartDashboard.putNumber(side + " SHOOTER POWER", output);
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
			return getSpeed();
		}
	}
}
