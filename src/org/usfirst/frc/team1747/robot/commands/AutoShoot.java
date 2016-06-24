package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.LeftShooter;
import org.usfirst.frc.team1747.robot.subsystems.RightShooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShoot extends Command {

	private static final double stallTime = 800, radsThreshold = .95;
	private DriveTrain drive;
	private LeftShooter leftShooter;
	private RightShooter rightShooter;
	private Intake intake;
	private Scooper scooper;
	private NetworkTable networkTable;
	private double startTime;
	private SDController.Positions position;
	private double turnValue;
	private double turnTime;
	private DriverStation driverStation;
	private boolean reset = false;

	// sets up AutoShoot
	public AutoShoot() {
		drive = Robot.getDriveTrain();
		leftShooter = Robot.getLeftShooter();
		rightShooter = Robot.getRightShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		networkTable = NetworkTable.getTable("imageProcessing");
		SmartDashboard.putNumber("StallTime", stallTime);
		SmartDashboard.putNumber("RadsThreshold", radsThreshold);
		SmartDashboard.putNumber("ShooterBaseline", 41.3);
		SmartDashboard.putBoolean("LastSecondShot", false);
		driverStation = DriverStation.getInstance();
		requires(leftShooter);
		requires(rightShooter);
		requires(drive);
		requires(intake);
		requires(scooper);
	}

	// initializes AutoShoot then prints out that it is running
	protected void initialize() {
		position = Robot.getSd().getAutonPosition();
		startTime = -1;
		turnTime = -1;
		turnValue = drive.getAutonTurn();
		drive.resetGyro();
		leftShooter.turnOffFlashlight();
	}

	protected void execute() {
		// Lowers scooper if not at lower limit
		if (!scooper.isAtLowerLimit()) {
			scooper.moveScooperDown();
		} else {
			scooper.scooperStop();
		}
		if (!intake.isAtTop()) {
			intake.moveLiftUp();
		} else {
			intake.liftStop();
		}
		if (position != SDController.Positions.NOTHING) {
			String direction = networkTable.getString("ShootDirection", "robotUnknown");
			if (turnTime - System.currentTimeMillis() <= -100) {
				double shooterRads = networkTable.getNumber("ShootRads", 0.0);
				turnTime = System.currentTimeMillis() + angleToTime(shooterRads);
				SmartDashboard.putNumber("angleToTime", angleToTime(shooterRads));
				reset = false;
			}
			if (SmartDashboard.getBoolean("LastSecondShot", false) && driverStation.isAutonomous()
					&& !direction.equals("robotUnknown") && driverStation.getMatchTime() < 3) {
				direction = "shoot";
			}
			// double boxDistance = networkTable.getNumber("ShootDistance", 0);
			SmartDashboard.putNumber("TimeLeftofTurn", turnTime - System.currentTimeMillis());
			if (direction.equals("left")) {
				if (leftShooter.isPidEnabled() || rightShooter.isPidEnabled()) {
					leftShooter.pidDisable();
					rightShooter.pidDisable();
				}
				leftShooter.setSpeed(0);
				rightShooter.setSpeed(0);
				if (turnTime - System.currentTimeMillis() >= 0) {
					drive.arcadeDrive(0.0, (-turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
				} else {
					drive.arcadeDrive(0, 0);
					if (!reset) {
						networkTable.putNumber("ShootRads", 0.0);
						reset = true;
					}
					// turnTime = -1;
				}
				startTime = -1;
			} else if (direction.equals("right")) {
				if (leftShooter.isPidEnabled() || rightShooter.isPidEnabled()) {
					leftShooter.pidDisable();
					rightShooter.pidDisable();
				}
				if (turnTime - System.currentTimeMillis() >= 0) {
					drive.arcadeDrive(0.0, (turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
				} else {
					drive.arcadeDrive(0, 0);
					if (!reset) {
						networkTable.putNumber("ShootRads", 0.0);
						reset = true;
					}
					// turnTime = -1;
				}
				leftShooter.setSpeed(0);
				rightShooter.setSpeed(0);
				startTime = -1;
			} else if (direction.equals("forward")) {
				if (leftShooter.isPidEnabled() || rightShooter.isPidEnabled()) {
					leftShooter.pidDisable();
					rightShooter.pidDisable();
				}
				leftShooter.setSpeed(0);
				rightShooter.setSpeed(0);
				drive.arcadeDrive(0.25, 0.0);
				startTime = -1;
			} else if (direction.equals("backward")) {
				if (leftShooter.isPidEnabled() || rightShooter.isPidEnabled()) {
					leftShooter.pidDisable();
					rightShooter.pidDisable();
				}
				leftShooter.setSpeed(0);
				rightShooter.setSpeed(0);
				drive.arcadeDrive(-0.25, 0.0);
				startTime = -1;
			} else if (direction.equals("shoot")) {
				drive.arcadeDrive(0, 0);

				if (startTime == -1) {
					startTime = System.currentTimeMillis();
				}
				if (startTime != -1 && System.currentTimeMillis() - startTime > 500 && (!leftShooter.isPidEnabled() && !rightShooter.isPidEnabled())) {
					leftShooter.setSetpoint(leftShooter.getTargetShooterSpeed());
					leftShooter.pidEnable();
					rightShooter.setSetpoint(rightShooter.getTargetShooterSpeed());
					rightShooter.pidEnable();
				}
				if (startTime != -1 && System.currentTimeMillis() - startTime > 750 && leftShooter.isAtTarget() && rightShooter.isAtTarget()) {
					intake.intakeBall();
				}
			} else if (direction.equals("unknown")) {
				// Add Lift If 1
				if (position == SDController.Positions.ONE || position == SDController.Positions.TWO
						|| position == SDController.Positions.THREE) {
					drive.arcadeDrive(0, 1.3 * turnValue);
				} else {
					drive.arcadeDrive(0, 1.3 * -turnValue);
				}
			}
		}
	}

	// returns true if auto mode is done, if not it returns false; uses
	// startTime, position, and the current system time
	protected boolean isFinished() {
		return (startTime != -1 && System.currentTimeMillis() - startTime > 2250)
				|| position == SDController.Positions.NOTHING;
	}

	// ends shoot and arcadeDrive
	protected void end() {
		drive.arcadeDrive(0, 0);
		leftShooter.setSpeed(0);
		leftShooter.pidDisable();
		rightShooter.setSpeed(0);
		rightShooter.pidDisable();
	}

	protected void interrupted() {
		end();
	}

	private double angleToTime(double shooterRads) {
		if (shooterRads == 0.0) {
			return -100;
		}
		return Math.max(
				(SmartDashboard.getNumber("RadsThreshold", radsThreshold) - shooterRads)
						* SmartDashboard.getNumber("StallTime", stallTime),
				SmartDashboard.getNumber("ShooterBaseline", 41.3));
	}

}
