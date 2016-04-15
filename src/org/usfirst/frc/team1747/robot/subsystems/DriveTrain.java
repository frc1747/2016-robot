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

	// Sets up CANTalons for drive train; maps the left and right LEDs
	public DriveTrain() {
		gyro = new AHRS(SPI.Port.kMXP);
		left = new DriveSide(RobotMap.LEFT_DRIVE_CIM_ONE, RobotMap.LEFT_DRIVE_CIM_TWO, RobotMap.LEFT_DRIVE_MINICIM,
				true);
		right = new DriveSide(RobotMap.RIGHT_DRIVE_CIM_ONE, RobotMap.RIGHT_DRIVE_CIM_TWO, RobotMap.RIGHT_DRIVE_MINICIM,
				false);

		// Left and right motors face each other, left is inverted
		for (double ignored : SIGMOIDSTRETCH) {
			straightTargetDeltas.add(0.0);
			rotationTargetDeltas.add(0.0);
		}
		// SmartDashboard.putNumber("DriveTrain LP", .015);
		// SmartDashboard.putNumber("DriveTrain LI", 0);
		// SmartDashboard.putNumber("DriveTrain LD", 0);
		// SmartDashboard.putNumber("DriveTrain RP", .015);
		// SmartDashboard.putNumber("DriveTrain RI", 0);
		// SmartDashboard.putNumber("DriveTrain RD", 0);
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
		return gyro.getAngle();
	}

	// This is a public void that logs smart dashboard.
	public void logToSmartDashboard() {
		SmartDashboard.putNumber("Left DriveTrain Speed", left.getSpeed());
		SmartDashboard.putNumber("Right DriveTrain Speed", right.getSpeed());
		// left.setPID(SmartDashboard.getNumber("DriveTrain LP", left.getP()),
		// SmartDashboard.getNumber("DriveTrain LI", left.getI()),
		// SmartDashboard.getNumber("DriveTrain LD", left.getD()));
		// right.setPID(SmartDashboard.getNumber("DriveTrain RP", right.getP()),
		// SmartDashboard.getNumber("DriveTrain RI", right.getI()),
		// SmartDashboard.getNumber("DriveTrain RD", right.getD()));
		SmartDashboard.putNumber("Turn Angle", gyro.getAngle());
		SmartDashboard.putNumber("Turn Velocity", gyro.getRate());
		SmartDashboard.putNumber("Left Distance", left.getNetDistance());
		SmartDashboard.putNumber("Right Distance", right.getNetDistance());
		teleopTurnDampening = SmartDashboard.getNumber("Turn Dampening", teleopTurnDampening);
		autonTurn = SmartDashboard.getNumber("Auton Turning", autonTurn);
	}

	// enables left and right PIDs
	public void enablePID() {
		left.enablePID();
		right.enablePID();
	}

	// sets up left and right setpoints
	public void setSetpoint(double targetSpeed) {
		left.setSetpoint(targetSpeed);
		right.setSetpoint(targetSpeed);
	}

	// disables left and right PID
	public void disablePID() {
		left.disablePID();
		right.disablePID();
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
		return left.isAtTarget() && right.isAtTarget();
	}

	// turns on the left and right LED lights

	// runs left and right PIDs
	public void runPID() {
		left.runPID();
		right.runPID();
	}

	public double getRightDistance() {
		return right.getNetDistance();
	}

	public double getLeftDistance() {
		return left.getNetDistance();
	}

	public void resetRightDistance() {
		right.resetNetDistance();
	}

	public void resetLeftDistance() {
		left.resetNetDistance();
	}

	// sets up constants for DriveSide
	class DriveSide {
		CANTalon cimOne, cimTwo, miniCim;
		double kP, kI, kD;
		double targetDistance;
		boolean pidEnabled;
		double integralError;
		double previousError;
		double netDistance;
		double time;
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

		// gets P
		public double getP() {
			return kP;
		}

		// gets I
		public double getI() {
			return kI;
		}

		// gets D
		public double getD() {
			return kD;
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

		// sets kP, kI, and kD
		public void setPID(double p, double i, double d) {
			kP = p;
			kI = i;
			kD = d;
		}

		// sets the target distance
		public void setSetpoint(double targetDistance) {
			this.targetDistance = targetDistance;
			cimTwo.setSetpoint(targetDistance);
		}

		// enable PID and clears target distance
		public void enablePID() {
			pidEnabled = true;
			time = System.currentTimeMillis();
			targetDistance = 0;
			integralError = 0;
			previousError = 0;
			netDistance = 0;
			time = 0;
		}

		// disables the PID
		public void disablePID() {
			pidEnabled = false;
		}

		// runs the PID using the constants kP, kI, and kD
		public void runPID() {
			if (pidEnabled) {
				double currentDistance = getNetDistance();
				double currentError = (targetDistance - currentDistance);
				integralError += currentError;
				double speed = kP * currentError + kI * integralError + kD * (currentError - previousError);
				previousError = currentError;
				if (speed > 1.0) {
					speed = 1.0;
				} else if (speed < -1.0) {
					speed = -1.0;
				}
				if (inverted) {
					SmartDashboard.putNumber("Drive pid left", speed);
				} else {
					SmartDashboard.putNumber("Drive pid right", speed);
				}
				set(speed);

			} else {
				set(0);
			}
		}

		// returns true if robot is at target, and false if robot is not at
		// target
		// all I do is win, win, no matter what
		public boolean isAtTarget() {
			// TODO: Verify grace distance
			return Math.abs(this.targetDistance - getNetDistance()) < 12;
		}

		// gets the net distance using input of time
		public double getNetDistance() {
			double pTime = time;
			time = System.currentTimeMillis();
			netDistance += (getSpeed() * (time - pTime)) / 1000.0;
			return netDistance;
		}

		public void resetNetDistance() {
			netDistance = 0;
		}
	}
}
// not 300 lines of code, take that DYLAN and JASON