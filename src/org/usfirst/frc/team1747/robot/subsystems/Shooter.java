package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import java.util.LinkedList;

public class Shooter extends Subsystem implements SDLogger {

	private ShooterSide left, right;
	private Solenoid flashlight;

	// set up left and right sides of the shooter, puts variables onto the
	// SmartDashboard
	public Shooter() {
		left = new ShooterSide(RobotMap.LEFT_SHOOTER_MOTOR_ONE, RobotMap.LEFT_SHOOTER_MOTOR_TWO, true,
				RobotMap.LEFT_COUNTER);
		right = new ShooterSide(RobotMap.RIGHT_SHOOTER_MOTOR_ONE, RobotMap.RIGHT_SHOOTER_MOTOR_TWO, false,
				RobotMap.RIGHT_COUNTER);
		flashlight = new Solenoid(RobotMap.FLASHLIGHT);
		SmartDashboard.putNumber("Target Shooter Speed", .6);
		SmartDashboard.putNumber("Shooter LP", 0);
		SmartDashboard.putNumber("Shooter LI", 0);
		SmartDashboard.putNumber("Shooter LD", 0);
		SmartDashboard.putNumber("Shooter LF", .005);
		SmartDashboard.putNumber("Shooter RP", 0);
		SmartDashboard.putNumber("Shooter RI", 0);
		SmartDashboard.putNumber("Shooter RD", 0);
		SmartDashboard.putNumber("Shooter RF", .005);
		SmartDashboard.putBoolean("Shooter PID Mode", true);
		SmartDashboard.putNumber("Shooter error margin", .025);
	}

	// enables PID
	public void enablePID() {
		left.enablePID();
		right.enablePID();
	}

	// disables PID
	public void disablePID() {
		left.disablePID();
		right.disablePID();
	}

	// runs PID
	public void runPID() {
		left.runPID();
		right.runPID();
	}

	public double getLeftSpeed() {
		return left.getSpeed();
	}

	public double getRightSpeed() {
		return right.getSpeed();
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

	// logs variables to SmartDashboard
	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Left Shooter Speed", getLeftSpeed());
		SmartDashboard.putNumber("Right Shooter Speed", getRightSpeed());
		left.setPID(SmartDashboard.getNumber("Shooter LP", left.getP()),
				SmartDashboard.getNumber("Shooter LI", left.getI()),
				SmartDashboard.getNumber("Shooter LD", left.getD()),
				SmartDashboard.getNumber("Shooter LF", left.getF()));
		right.setPID(SmartDashboard.getNumber("Shooter RP", right.getP()),
				SmartDashboard.getNumber("Shooter RI", right.getI()),
				SmartDashboard.getNumber("Shooter RD", right.getD()),
				SmartDashboard.getNumber("Shooter RF", right.getF()));
	}

	public void setSetpoint(double targetSpeed) {
		left.setSetpoint(targetSpeed);
		right.setSetpoint(targetSpeed);
	}

	public void shoot(double speed) {
		left.set(speed);
		right.set(speed);
	}

	public boolean isAtTarget() {
		return left.isAtTarget() && right.isAtTarget();
	}

	private class ShooterSide {
		private static final int errBuffSize = 10;
		CANTalon motorOne, motorTwo;
		Counter counter;
		double kP, kI, kD, kF, targetSpeed, integralError, previousError, totalError;
		LinkedList<Double> errorBuffer;
		long previousTime;

		// sets up both sides of the shooter
		public ShooterSide(int motorOneId, int motorTwoId, boolean inverted, int counterId) {
			motorOne = new CANTalon(motorOneId);
			motorTwo = new CANTalon(motorTwoId);
			motorOne.changeControlMode(TalonControlMode.Voltage);
			motorTwo.changeControlMode(TalonControlMode.Voltage);
			motorOne.setInverted(inverted);
			motorTwo.setInverted(inverted);
			counter = new Counter();
			counter.setUpSource(counterId);
			counter.setUpDownCounterMode();
			counter.setPIDSourceType(PIDSourceType.kRate);
			counter.setDistancePerPulse(1.0);
			targetSpeed = 0;
			integralError = 0;
			previousError = 0;
			previousTime = 0;
			totalError = 0;
			errorBuffer = new LinkedList<>();
		}

		// TODO:Check this % tolerance
		public boolean isAtTarget() {
			return Math.abs((getAvgSpeed() - targetSpeed) / targetSpeed) < SmartDashboard
					.getNumber("Shooter error margin", .025);
		}

		public double getP() {
			return kP;
		}

		public double getI() {
			return kI;
		}

		public double getD() {
			return kD;
		}

		public double getF() {
			return kF;
		}

		// makes function getspeed which returns the counter rate/10,000
		public double getSpeed() {
			return counter.getRate() / 100.0;
		}

		public double getAvgSpeed() {
			if (errorBuffer.size() == 0) {
				return 0;
			} else {
				return totalError / errorBuffer.size();
			}
		}

		// Variation of
		// http://www.chiefdelphi.com/forums/showpost.php?p=690837&postcount=13
		// runs PID and puts left and right speeds on SmartDashboard
		public void runPID() {
			double currentSpeed = getSpeed();
			double deltaTime = (System.currentTimeMillis() - previousTime) / 1000.0;
			double currentError = targetSpeed - currentSpeed;
			errorBuffer.addFirst(currentError);
			totalError += currentError;
			if (errorBuffer.size() > errBuffSize) {
				totalError -= errorBuffer.removeLast();
			}
			integralError += currentError * deltaTime;
			double derivative = 0;
			if (deltaTime > .0001) {
				derivative = (currentError - previousError) / deltaTime;
			}
			double speed = kF * targetSpeed + kP * currentError + kI * integralError + kD * derivative;
			previousError = currentError;
			previousTime = System.currentTimeMillis();
			if (speed > 1.0) {
				speed = 1.0;
			} else if (speed < -1.0) {
				speed = -1.0;
			}
			if (motorOne.getInverted()) {
				SmartDashboard.putNumber("Shooter pid left", speed);
				SmartDashboard.putNumber("Left Delta Time", deltaTime);
				System.out.println("P: " + (kP * currentError) + "\nI: " + (kI * integralError) + "\nD: "
						+ (kD * derivative) + "\ndeltaTime: " + deltaTime + "\ncurrentError: " + currentError
						+ "\npreviousError:" + previousError);
			} else {
				SmartDashboard.putNumber("Shooter pid right", speed);
			}
			set(speed);
		}

		// sets motor one and two speeds
		public void set(double speed) {
			speed *= 12.0;
			motorOne.set(speed);
			motorTwo.set(speed);
		}

		// sets kP, kI, kD, and kF
		public void setPID(double p, double i, double d, double f) {
			kP = p;
			kI = i;
			kD = d;
			kF = f;
		}

		// sets the target speed
		public void setSetpoint(double targetSpeed) {
			this.targetSpeed *= 12.0;
		}

		// enables the PID
		public void enablePID() {
			previousTime = System.currentTimeMillis();
		}

		// turns off the PID and clears target speed
		public void disablePID() {
			targetSpeed = 0;
			integralError = 0;
			previousError = 0;
			previousTime = 0;
		}
	}
}
