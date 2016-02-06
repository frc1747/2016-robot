package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem implements SDLogger {

	class ShooterSide implements PIDOutput {
		CANTalon motorOne, motorTwo;
		Counter counter;
		PIDController pidController;
		double kP, kI, kD;

		public ShooterSide(int motorOneId, int motorTwoId, boolean inverted, int counterId) {
			motorOne = new CANTalon(motorOneId);
			motorTwo = new CANTalon(motorTwoId);
			motorOne.setInverted(inverted);
			motorTwo.setInverted(inverted);
			counter = new Counter();
			counter.setUpSource(counterId);
			counter.setUpDownCounterMode();
			counter.setPIDSourceType(PIDSourceType.kRate);
			counter.setDistancePerPulse(1.0);
			pidController = new PIDController(kP, kI, kD, counter, this);
			pidController.disable();
			pidController.setOutputRange(-.8, .8);
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
			return counter.getRate();
			// return 1.0 / counter.getPeriod();
		}

		public void pidWrite(double output) {
			if (motorOne.getInverted()) {
				SmartDashboard.putNumber("left", output);
			} else {
				SmartDashboard.putNumber("right", output);
			}
			set(output);
		}

		public void set(double speed) {
			motorOne.set(speed);
			motorTwo.set(speed);
		}

		public void setPID(double p, double i, double d) {
			if (p != kP || i != kI || d != kD) {
				pidController.setPID(p, i, d);
				kP = p;
				kI = i;
				kD = d;
			}
		}

		public void setSetpoint(double targetSpeed) {
			pidController.setSetpoint(targetSpeed);
		}

		public void enablePID() {
			pidController.reset();
			pidController.enable();
		}

		public void disablePID() {
			pidController.disable();
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

		SmartDashboard.putNumber("Target Shooter Speed", .6);
		SmartDashboard.putNumber("Shooter LP", .05);
		SmartDashboard.putNumber("Shooter LI", 0);
		SmartDashboard.putNumber("Shooter LD", 0);
		SmartDashboard.putNumber("Shooter RP", .05);
		SmartDashboard.putNumber("Shooter RI", 0);
		SmartDashboard.putNumber("Shooter RD", 0);
	}

	public void enablePID() {
		left.enablePID();
		right.enablePID();
	}

	public void disablePID() {
		left.disablePID();
		right.disablePID();
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
