package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;
import org.usfirst.frc.team1747.robot.commands.TeleopDrive;

import java.util.LinkedList;

public class DriveTrain extends Subsystem implements SDLogger {
	static final double[] SIGMOIDSTRETCH = { 0.03, 0.06, 0.09, 0.1, 0.11, 0.12, 0.11, 0.1, 0.09, 0.06, 0.03 };
	DriveSide left, right;
	LinkedList<Double> straightTargetDeltas = new LinkedList<Double>();
	LinkedList<Double> rotationTargetDeltas = new LinkedList<Double>();
	double pStraightTarget = 0.0, pRotationTarget = 0.0, prevTargetStraight = 0.0, prevTargetRotation = 0.0;

	// Sets up CANTalons for drive train
	public DriveTrain() {
		left = new DriveSide(RobotMap.LEFT_DRIVE_CIM_ONE, RobotMap.LEFT_DRIVE_CIM_TWO, RobotMap.LEFT_DRIVE_MINICIM,
				true);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_CIM_ONE, RobotMap.RIGHT_DRIVE_CIM_TWO, RobotMap.RIGHT_DRIVE_MINICIM,
				false);
		// Left and right motors face each other, left is inverted
		for (int j = 0; j < SIGMOIDSTRETCH.length; j++) {
			straightTargetDeltas.add(0.0);
			rotationTargetDeltas.add(0.0);
		}
	}

	// Sets up the tank drive.
	public void tankDrive(double leftSpeed, double rightSpeed) {
		left.set(leftSpeed);
		right.set(rightSpeed);
	}

	public void arcadeDrive(double straight, double turn) {
		tankDrive(straight + turn, straight - turn);
	}

	// This is smooth drive. bush did 9II
	public void smoothDrive(double targetStraight, double targetRotation) {
		straightTargetDeltas.removeLast();
		rotationTargetDeltas.removeLast();
		straightTargetDeltas.addFirst(targetStraight - prevTargetStraight);
		rotationTargetDeltas.addFirst(targetRotation - prevTargetRotation);
		prevTargetStraight = targetStraight;
		prevTargetRotation = targetRotation;
		for (int i = 0; i < SIGMOIDSTRETCH.length; i++) {
			pStraightTarget += straightTargetDeltas.get(i) * SIGMOIDSTRETCH[i];
			pRotationTarget += rotationTargetDeltas.get(i) * SIGMOIDSTRETCH[i];
		}
		arcadeDrive(pStraightTarget, pRotationTarget);
	}

	@SuppressWarnings("Duplicates")
	public void plateauDrive(double straight, double turn) {
		if (Math.abs(straight) < .01) {
			straight = 0.0;
		} else if (Math.abs(straight) < 0.5) {
			straight = (Math.abs(straight) / straight) * 0.5 / (1 + Math.exp(-20.0 * (Math.abs(straight) - 0.2)));
		} else {
			straight = (Math.abs(straight) / straight)
					* (0.5 / (1 + Math.exp(-20.0 * (Math.abs(straight) - 0.8)) + 0.5));
		}
		if (Math.abs(turn) < .01) {
			turn = 0.0;
		} else if (Math.abs(turn) < 0.5) {
			turn = (Math.abs(turn) / turn) * 0.5 / (1 + Math.exp(-20.0 * (Math.abs(turn) - 0.2)));
		} else {
			turn = (Math.abs(turn) / turn) * (0.5 / (1 + Math.exp(-20.0 * (Math.abs(turn) - 0.8)) + 0.5));
		}
		SmartDashboard.putNumber("Straight", straight);
		SmartDashboard.putNumber("Turn", turn);
		tankDrive(straight + turn, straight - turn);
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive());
	}

	// This is a public void that logs smart dashboard.
	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Left DriveTrain Speed", left.getSpeed());
		SmartDashboard.putNumber("Right DriveTrain Speed", right.getSpeed());
		left.setPID(SmartDashboard.getNumber("DriveTrain LP", left.getP()),
				SmartDashboard.getNumber("DriveTrain LI", left.getI()),
				SmartDashboard.getNumber("DriveTrain LD", left.getD()));
		right.setPID(SmartDashboard.getNumber("DriveTrain RP", right.getP()),
				SmartDashboard.getNumber("DriveTrain RI", right.getI()),
				SmartDashboard.getNumber("DriveTrain RD", right.getD()));
	}

	public void enablePID() {
		left.enablePID();
		right.enablePID();
	}

	public void setSetpoint(double targetSpeed) {
		left.setSetpoint(targetSpeed);
		right.setSetpoint(targetSpeed);
	}

	public void runPID() {
		left.runPID();
		right.runPID();
	}

	public void disablePID() {
		left.disablePID();
		right.disablePID();
	}

	public boolean isAtTarget() {
		return left.isAtTarget() && right.isAtTarget();
	}

	class DriveSide {
		CANTalon cimOne, cimTwo, miniCim;
		double kP, kI, kD;
		boolean pidEnabled;
		double targetDistance;
		double integralError;
		double previousError;
		double netDistance;
		double distance = 0;
		double pDistance = 0;
		double time = 0;
		double pTime = 0;
		double accumulator = 0;

		public DriveSide(int cimOneID, int cimTwoID, int miniCimID, boolean inverted) {
			cimOne = new CANTalon(cimOneID);
			cimTwo = new CANTalon(cimTwoID);
			miniCim = new CANTalon(miniCimID);
			cimOne.setInverted(inverted);
			cimTwo.setInverted(inverted);
			miniCim.setInverted(inverted);
			pidEnabled = false;
			targetDistance = 0;
			integralError = 0;
			previousError = 0;
			distance = 0;
			pDistance = 0;
			time = 0;
			pTime = 0;
			accumulator = 0;
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

		@SuppressWarnings("Duplicates")
		public void runPID() {
			double currentDistance = getNetDistance();
			double currentError = (currentDistance - targetDistance);
			integralError += currentError;
			// Motor Voltage = Kp*error + Ki*error_sum + Kd*(error-error_last)
			double speed = kP * currentError + kI * integralError + kD * (currentError - previousError);
			previousError = currentError;
			if (cimOne.getInverted())
				SmartDashboard.putNumber("left", speed);
			else
				SmartDashboard.putNumber("right", speed);
			set(speed);
		}

		public double getSpeed() {
			return cimTwo.getSpeed();
		}

		private double getNetDistance() {
			pTime = time;
			time = Timer.getFPGATimestamp();
			accumulator += (getSpeed() * (time - pTime));
			return accumulator;
		}

		public void set(double speed) {
			cimOne.set(speed);
			cimTwo.set(speed);
			miniCim.set(speed);
		}

		public void setPID(double p, double i, double d) {
			kP = p;
			kI = i;
			kD = d;
		}

		public void setSetpoint(double targetDistance) {
			this.targetDistance = targetDistance;
		}

		public boolean isAtTarget() {
			return targetDistance == getNetDistance();
		}

		public void enablePID() {
			pidEnabled = true;
		}

		public void disablePID() {
			pidEnabled = false;
			targetDistance = 0;
			integralError = 0;
			previousError = 0;
			pTime = 0;
			accumulator = 0;
			distance = 0;
			pDistance = 0;
			time = 0;
			pTime = 0;
		}
	}
}
