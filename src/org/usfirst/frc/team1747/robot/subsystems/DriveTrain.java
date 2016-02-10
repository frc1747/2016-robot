package org.usfirst.frc.team1747.robot.subsystems;

import java.util.LinkedList;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;
import org.usfirst.frc.team1747.robot.commands.TeleopDrive;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements SDLogger {
	CANTalon leftCimOne, leftCimTwo, leftMiniCim;
	CANTalon rightCimOne, rightCimTwo, rightMiniCim;

	static final double[] SIGMOIDSTRETCH = { 0.03, 0.06, 0.09, 0.1, 0.11, 0.12, 0.11, 0.1, 0.09, 0.06, 0.03 };

	LinkedList<Double> straightTargetDeltas = new LinkedList<Double>();
	LinkedList<Double> rotationTargetDeltas = new LinkedList<Double>();

	double pStraightTarget = 0.0, pRotationTarget = 0.0, prevTargetStraight = 0.0, prevTargetRotation = 0.0;

	private static final double kP = 0;
	private static final double kI = 0;
	private static final double kD = 0;

	// Sets up CANTalons for drive train
	public DriveTrain() {
		leftCimOne = new CANTalon(RobotMap.LEFT_DRIVE_CIM_ONE);
		setupPID(leftCimOne, CANTalon.TalonControlMode.Position);
		leftCimTwo = new CANTalon(RobotMap.LEFT_DRIVE_CIM_TWO);
		setupPID(leftCimTwo, CANTalon.TalonControlMode.Follower);
		leftMiniCim = new CANTalon(RobotMap.LEFT_DRIVE_MINICIM);
		setupPID(leftMiniCim, CANTalon.TalonControlMode.Follower);
		rightCimOne = new CANTalon(RobotMap.RIGHT_DRIVE_CIM_ONE);
		setupPID(rightCimOne, CANTalon.TalonControlMode.Position);
		rightCimTwo = new CANTalon(RobotMap.RIGHT_DRIVE_CIM_TWO);
		setupPID(rightCimTwo, CANTalon.TalonControlMode.Follower);
		rightMiniCim = new CANTalon(RobotMap.RIGHT_DRIVE_MINICIM);
		setupPID(rightMiniCim, CANTalon.TalonControlMode.Follower);
		// Left and right motors face each other
		leftCimOne.setInverted(true);
		leftCimTwo.setInverted(true);
		leftMiniCim.setInverted(true);
		for (int j = 0; j < SIGMOIDSTRETCH.length; j++) {
			straightTargetDeltas.add(0.0);
			rotationTargetDeltas.add(0.0);
		}
	}

	// Sets up the tank drive.
	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftCimOne.set(leftSpeed);
		leftCimTwo.set(leftSpeed);
		leftMiniCim.set(leftSpeed);
		rightCimOne.set(rightSpeed);
		rightCimTwo.set(rightSpeed);
		rightMiniCim.set(rightSpeed);
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

	public void plateauDrive(double straight, double turn) {
		if (Math.abs(straight) < .01) {
			straight = 0.0;
		} else if (Math.abs(straight) < 0.5) {
			straight = (Math.abs(straight) / straight) * 0.5 / (1 + Math.exp(-20.0 * (straight - 0.2)));
		} else {
			straight = (Math.abs(straight) / straight) * (0.5 / (1 + Math.exp(-20.0 * (straight - 0.8)) + 0.5));
		}
		if (Math.abs(turn) < .01) {
			turn = 0.0;
		} else if (Math.abs(turn) < 0.5) {
			turn = (Math.abs(turn) / turn) * 0.5 / (1 + Math.exp(-20.0 * (turn - 0.2)));
		} else {
			turn = (Math.abs(turn) / turn) * (0.5 / (1 + Math.exp(-20.0 * (turn - 0.8)) + 0.5));
		}
		SmartDashboard.putNumber("Straight", straight);
		SmartDashboard.putNumber("Turn", turn);
		tankDrive(straight + turn, straight - turn);
	}

	public void setupPID(CANTalon talon, CANTalon.TalonControlMode controlMode) {
		// talon.setControlMode(controlMode.getValue());
		// talon.setPID(kP, kI, kD);
	}

	public double getLeftSpeed() {
		return leftCimTwo.getSpeed();
	}

	public double getRightSpeed() {
		return -rightCimTwo.getSpeed();
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive());

	}

	// This is a public void that logs smart dashboard.
	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Left DriveTrain Speed", getLeftSpeed());
		SmartDashboard.putNumber("Right DriveTrain Speed", getRightSpeed());
	}
}
