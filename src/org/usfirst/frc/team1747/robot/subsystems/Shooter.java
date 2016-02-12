package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem implements SDLogger {

	class ShooterSide {
		CANTalon motorOne, motorTwo;
		Counter counter;
		double kP, kI, kD;
		boolean pidEnabled;
		double targetSpeed;
		double integralError;
		double previousError;

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
			pidEnabled = false;
			targetSpeed = 0;
			integralError = 0;
			previousError = 0;
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

		public double getSpeed() {
			return counter.getRate() / 10000.0;
		}

		public void runPID() {
			double currentSpeed = getSpeed();
			double currentError = (currentSpeed - targetSpeed);
			integralError += currentError;
			// Motor Voltage = Kp*error + Ki*error_sum + Kd*(error-error_last)
			double speed = kP * currentError + kI * integralError + kD * (currentError - previousError);
			previousError = currentError;
			if (motorOne.getInverted())
				SmartDashboard.putNumber("left", speed);
			else
				SmartDashboard.putNumber("right", speed);
			set(speed);
		}

		public void set(double speed) {
			speed *= 12.0;
			motorOne.set(speed);
			motorTwo.set(speed);
		}

		public void setPID(double p, double i, double d) {
			kP = p;
			kI = i;
			kD = d;
		}

		public void setSetpoint(double targetSpeed) {
			this.targetSpeed = targetSpeed;
		}

		public void enablePID() {
			pidEnabled = true;
		}

		public void disablePID() {
			pidEnabled = false;
			targetSpeed = 0;
			integralError = 0;
			previousError = 0;
		}
	}

	ShooterSide left, right;
	Solenoid led;

	public Shooter() {
		System.out.println("ShooterMotor created");
		left = new ShooterSide(RobotMap.LEFT_SHOOTER_MOTOR_ONE, RobotMap.LEFT_SHOOTER_MOTOR_TWO, true,
				RobotMap.LEFT_COUNTER);
		right = new ShooterSide(RobotMap.RIGHT_SHOOTER_MOTOR_ONE, RobotMap.RIGHT_SHOOTER_MOTOR_TWO, false,
				RobotMap.RIGHT_COUNTER);
		led = new Solenoid(RobotMap.LED);
		turnOnLED();
		SmartDashboard.putNumber("Target Shooter Speed", .6);
		SmartDashboard.putNumber("Shooter LP", .05);
		SmartDashboard.putNumber("Shooter LI", 0);
		SmartDashboard.putNumber("Shooter LD", 0);
		SmartDashboard.putNumber("Shooter RP", .05);
		SmartDashboard.putNumber("Shooter RI", 0);
		SmartDashboard.putNumber("Shooter RD", 0);
		SmartDashboard.putBoolean("Shooter PID Mode", true);
	}

	public void enablePID() {
		left.enablePID();
		right.enablePID();
	}

	public void disablePID() {
		left.disablePID();
		right.disablePID();
	}

	public void runPID() {
		left.runPID();
		right.runPID();
	}

	@Override
	protected void initDefaultCommand() {
	}

	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Left Shooter Speed", left.getSpeed());
		SmartDashboard.putNumber("Right Shooter Speed", right.getSpeed());
		left.setPID(SmartDashboard.getNumber("Shooter LP", left.getP()),
				SmartDashboard.getNumber("Shooter LI", left.getI()),
				SmartDashboard.getNumber("Shooter LD", left.getD()));
		right.setPID(SmartDashboard.getNumber("Shooter RP", right.getP()),
				SmartDashboard.getNumber("Shooter RI", right.getI()),
				SmartDashboard.getNumber("Shooter RD", right.getD()));
	}

	public void setSetpoint(double targetSpeed) {
		left.setSetpoint(targetSpeed);
		right.setSetpoint(targetSpeed);
	}

	public void shoot(double speed) {
		left.set(speed);
		right.set(speed);
	}

	public void turnOffLED() {
		led.set(false);
	}

	public void turnOnLED() {
		led.set(true);
	}
}
