package org.usfirst.frc.team1747.robot.subsystems;

import java.util.LinkedList;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;
import org.usfirst.frc.team1747.robot.commands.TeleopDrive;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements SDLogger {
	double integralError, previousError, totalPreviousAngles;
	LinkedList<Double> previousAngles;
	long previousTime;
	private static final double[] SIGMOIDSTRETCH = { 0.03, 0.06, 0.09, 0.1, 0.11, 0.12, 0.11, 0.1, 0.09, 0.06, 0.03 };
	private DriveSide left;
	private DriveSide right;
	private AHRS gyro;
	private LinkedList<Double> straightTargetDeltas = new LinkedList<>();
	private LinkedList<Double> rotationTargetDeltas = new LinkedList<>();
	private double pStraightTarget = 0.0;
	private double pRotationTarget = 0.0;
	private double prevTargetStraight = 0.0;
	private double prevTargetRotation = 0.0;
	private double autonTurn;
	private double teleopTurnDampening;
	private double kP = 0.02, kI = 0, kD = 0, angleErrorMargin = .005;
	private boolean pidEnabled;
	private static final int errBuffSize = 1;

	// Sets up CANTalons for drive train; maps the left and right LEDs
	public DriveTrain() {
		gyro = new AHRS(SPI.Port.kMXP);
		left = new DriveSide(RobotMap.LEFT_DRIVE_CIM_ONE, RobotMap.LEFT_DRIVE_CIM_TWO, RobotMap.LEFT_DRIVE_MINICIM,
				true);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_CIM_ONE, RobotMap.RIGHT_DRIVE_CIM_TWO, RobotMap.RIGHT_DRIVE_MINICIM,
				false);
		gyro = new AHRS(SPI.Port.kMXP);
		// Left and right motors face each other, left is inverted
		for (double ignored : SIGMOIDSTRETCH) {
			straightTargetDeltas.add(0.0);
			rotationTargetDeltas.add(0.0);
		}
		previousAngles = new LinkedList<Double>();
		SmartDashboard.putNumber("DriveTrain P", kP);
		SmartDashboard.putNumber("DriveTrain I", kI);
		SmartDashboard.putNumber("DriveTrain D", kD);
		SmartDashboard.putNumber("DriveTrain Error Margin", angleErrorMargin);
		SmartDashboard.putNumber("Turn Dampening", 0.9);
		SmartDashboard.putNumber("Auton Turning", 0.350);
		SmartDashboard.putBoolean("BackUpInAuto", false);
	}

	// Sets up the tank drive using left and right speed
	public void tankDrive(double leftSpeed, double rightSpeed) {
		left.set(leftSpeed);
		right.set(rightSpeed);
	}

	// sets up arcade drive
	public void arcadeDrive(double straight, double turn) {
		tankDrive(straight + turn, straight - turn);
	}

	// This is smooth drive.
	public void smoothDrive(double targetStraight, double targetRotation) {
		if (straightTargetDeltas.size() > SIGMOIDSTRETCH.length) {
			straightTargetDeltas.removeLast();
		}
		if (rotationTargetDeltas.size() > SIGMOIDSTRETCH.length) {
			rotationTargetDeltas.removeLast();
		}
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

	public double getTeleopTurnDampener() {
		return teleopTurnDampening;
	}

	public double getAutonTurn() {
		return autonTurn;
	}

	public void resetGyro() {
		gyro.zeroYaw();
	}

	public double getTurnAngle() {
		return gyro.getYaw();
	}

	// This is a public void that logs smart dashboard.
	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Left DriveTrain Speed", left.getSpeed());
		SmartDashboard.putNumber("Right DriveTrain Speed", right.getSpeed());
		setPID(SmartDashboard.getNumber("DriveTrain P", kP), SmartDashboard.getNumber("DriveTrain I", kI),
				SmartDashboard.getNumber("DriveTrain D", kD));
		SmartDashboard.putNumber("Turn Angle", getTurnAngle());
		SmartDashboard.putNumber("Turn Velocity", gyro.getRate());
		teleopTurnDampening = SmartDashboard.getNumber("Turn Dampening", teleopTurnDampening);
		autonTurn = SmartDashboard.getNumber("Auton Turning", autonTurn);
	}

	// enables left and right PIDs
	public void enablePID() {
		pidEnabled = true;
		previousTime = System.currentTimeMillis();
	}

	double targetAngle;

	// sets up left and right setpoints
	public void setSetpoint(double targetAngle) {
		this.targetAngle = targetAngle;
	}

	// disables left and right PID
	public void disablePID() {
		integralError = 0;
		previousError = 0;
		previousTime = 0;
		totalPreviousAngles = 0;
		previousAngles.clear();
		targetAngle = 0;
		pidEnabled = false;
	}

	public boolean isPIDEnabled() {
		return pidEnabled;
	}

	// enables Ramping
	public void enableRamping() {
		left.enableRamping();
		right.enableRamping();
	}

	// disables ramping
	public void disableRamping() {
		left.disableRamping();
		right.disableRamping();
	}

	// determines if robot is at target
	public boolean isAtTarget() {
		System.out.println("Tolerance: " + ((getTurnAngle() - this.targetAngle) / this.targetAngle));
		return Math.abs((getTurnAngle() - this.targetAngle) / this.targetAngle) < SmartDashboard
				.getNumber("DriveTrain Error Margin", angleErrorMargin);

	}

	// runs left and right PIDs
	public void runPID() {
		double currentAngle = getTurnAngle();
		double deltaTime = (System.currentTimeMillis() - previousTime) / 1000.0;
		double currentError = targetAngle - currentAngle;
		double derivative = 0;
		previousAngles.addFirst(currentAngle);
		totalPreviousAngles += currentAngle;
		if (previousAngles.size() > errBuffSize) {
			totalPreviousAngles -= previousAngles.removeLast();
		}
		integralError += currentError * deltaTime;
		if (deltaTime > .0001) {
			derivative = (currentError - previousError) / deltaTime;
		}
		double speed = kP * currentError + kI * integralError + kD * derivative;
		previousError = currentError;
		previousTime = System.currentTimeMillis();
		if (speed > 0.5) {
			speed = 0.5;
			System.out.println("Speed > 0.5");
		} else if (speed < -0.5) {
			speed = -0.5;
			System.out.println("Speed < -0.5");
		}
		System.out.println("P: " + (kP * currentError));
		System.out.println("I: " + (kI * integralError));
		System.out.println("D: " + (kD * derivative));
		System.out.println("deltaTime: " + deltaTime);
		System.out.println("currentError: " + currentError);
		System.out.println("previousError: " + previousError);
		System.out.println("currentAngle: " + currentAngle);
		System.out.println("targetSpeed: " + targetAngle);
		System.out.println("Calculated turnSpeed: " + speed);
		arcadeDrive(0, speed);
	}

	public void setPID(double kp, double ki, double kd) {
		this.kP = kp;
		this.kI = ki;
		this.kD = kd;
	}

	// sets up constants for DriveSide
	class DriveSide {
		CANTalon cimOne, cimTwo, miniCim;
		boolean inverted;

		// sets up the control modes for the talons and inverts some cims and
		// miniCims
		public DriveSide(int cimOneID, int cimTwoID, int miniCimID, boolean inverted) {
			cimOne = new CANTalon(cimOneID);
			cimTwo = new CANTalon(cimTwoID);
			miniCim = new CANTalon(miniCimID);
			cimTwo.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			// cimTwo.setProfile(0);
			cimTwo.setPIDSourceType(PIDSourceType.kDisplacement);
			// cimTwo.setVoltageRampRate(18);
			cimTwo.changeControlMode(TalonControlMode.Voltage);
			cimOne.changeControlMode(TalonControlMode.Follower);
			miniCim.changeControlMode(TalonControlMode.Follower);
			cimOne.set(cimTwoID);
			miniCim.set(cimTwoID);
			cimOne.setInverted(inverted);
			cimTwo.setInverted(inverted);
			miniCim.setInverted(inverted);
			this.inverted = inverted;
		}

		// enables ramping
		public void enableRamping() {
			cimTwo.setVoltageRampRate(24);
		}

		// disables ramping
		public void disableRamping() {
			cimTwo.setVoltageRampRate(0);
		}

		// returns the speed of cimtwo
		public double getSpeed() {
			// Remove when encoder repaired
			return cimTwo.getSpeed() * .04295 * (inverted ? 1 : -(4 / 3));
		}

		// sets speed of cimTwo
		public void set(double speed) {
			speed *= 12.0;
			cimTwo.set(speed);
		}
	}
}
// not 300 lines of code, take that DYLAN and JASON