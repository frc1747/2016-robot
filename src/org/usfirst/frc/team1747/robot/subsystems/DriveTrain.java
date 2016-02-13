package org.usfirst.frc.team1747.robot.subsystems;

import java.util.LinkedList;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;
import org.usfirst.frc.team1747.robot.commands.TeleopDrive;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements SDLogger {
	static final double[] SIGMOIDSTRETCH = { 0.03, 0.06, 0.09, 0.1, 0.11, 0.12, 0.11, 0.1, 0.09, 0.06, 0.03 };
	DriveSide left, right;
	LinkedList<Double> straightTargetDeltas = new LinkedList<Double>();
	LinkedList<Double> rotationTargetDeltas = new LinkedList<Double>();
	double pStraightTarget = 0.0, pRotationTarget = 0.0, prevTargetStraight = 0.0, prevTargetRotation = 0.0;
	Solenoid glowLeft;
	Solenoid glowRight;
	// Sets up CANTalons for drive train
	public DriveTrain() {
		left = new DriveSide(RobotMap.LEFT_DRIVE_CIM_ONE, RobotMap.LEFT_DRIVE_CIM_TWO, RobotMap.LEFT_DRIVE_MINICIM,
				true);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_CIM_ONE, RobotMap.RIGHT_DRIVE_CIM_TWO, RobotMap.RIGHT_DRIVE_MINICIM,
				false);
		glowLeft = new Solenoid(RobotMap.ROBOT_GLOW_LEFT);
		glowRight = new Solenoid(RobotMap.ROBOT_GLOW_RIGHT);
		turnOnGlow();
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

	public void disablePID() {
		left.disablePID();
		right.disablePID();
	}

	public void enableRamping() {
		left.enableRamping();
		right.enableRamping();
	}

	public void disableRamping() {
		left.disableRamping();
		right.disableRamping();
	}

	class DriveSide {
		CANTalon cimOne, cimTwo, miniCim;
		double kP, kI, kD;
		double targetDistance;

		public DriveSide(int cimOneID, int cimTwoID, int miniCimID, boolean inverted) {
			cimOne = new CANTalon(cimOneID);
			cimTwo = new CANTalon(cimTwoID);
			miniCim = new CANTalon(miniCimID);
			cimTwo.setPIDSourceType(PIDSourceType.kDisplacement);
			cimTwo.setVoltageRampRate(18);
			cimTwo.changeControlMode(TalonControlMode.Voltage);
			cimOne.changeControlMode(TalonControlMode.Follower);
			miniCim.changeControlMode(TalonControlMode.Follower);
			cimOne.set(cimTwoID);
			miniCim.set(cimTwoID);
			cimOne.setInverted(inverted);
			cimTwo.setInverted(inverted);
			miniCim.setInverted(inverted);
		}

		public void enableRamping() {
			cimTwo.setVoltageRampRate(24);
		}

		public void disableRamping() {
			cimTwo.setVoltageRampRate(0);
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
			return cimTwo.getSpeed();
		}

		public void set(double speed) {
			speed *= 12.0;
			cimTwo.set(speed);
		}

		public void setPID(double p, double i, double d) {
			kP = p;
			kI = i;
			kD = d;
		}

		public void setSetpoint(double targetDistance) {
			this.targetDistance = targetDistance;
			cimTwo.setSetpoint(targetDistance);
		}

		public void enablePID() {
			cimTwo.changeControlMode(TalonControlMode.Position);
			cimOne.setPID(kP, kI, kD);
			cimTwo.setPID(kP, kI, kD);
			miniCim.setPID(kP, kI, kD);
		}

		public void disablePID() {
			cimTwo.changeControlMode(TalonControlMode.Voltage);
			cimOne.setPID(0, 0, 0);
			cimTwo.setPID(0, 0, 0);
			miniCim.setPID(0, 0, 0);
			this.targetDistance = 0;
		}

		public boolean isAtTarget() {
			// TODO: Verify grace distance
			return Math.abs(this.targetDistance - cimTwo.get()) < .1;
		}
	}

	public boolean isAtTarget() {
		return left.isAtTarget() && right.isAtTarget();
	}
	
	public void turnOnGlow() {
		glowRight.set(true);
		glowLeft.set(true);
	}
	
	public void turnOffGlow() {
		glowRight.set(false);
		glowLeft.set(false);
	}
}
