package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.PrecisionCyborgController;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Gyro;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShoot extends Command {

	DriveTrain drive;
	Shooter shoot;
	Intake intake;
	Scooper scooper;
	PrecisionCyborgController auxController;
	NetworkTable networkTable;
	double speed;
	double startTime;
	int position;
	double turnValue;
	double turnTime;
	double gyroAngle;
	double turnAngle;
	public Gyro gyro;
	DriverStation driverStation;

	// sets up AutoShoot
	public AutoShoot() {
		drive = Robot.getDriveTrain();
		shoot = Robot.getShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		gyro = Robot.getGyro();
		networkTable = NetworkTable.getTable("imageProcessing");
		SmartDashboard.putNumber("StallTime", 600);
		SmartDashboard.putNumber("RadsThreshhold", 1.0);
		SmartDashboard.putNumber("Gyro Angle", 0.0);
		driverStation = DriverStation.getInstance();
		requires(shoot);
		requires(drive);
		requires(intake);
		requires(scooper);
	}

	// initializes AutoShoot then prints out that it is running
	protected void initialize() {
		speed = SmartDashboard.getNumber("Target Shooter Speed", .6);
		position = Robot.getSd().getAutonPosition();
		startTime = -1;
		turnTime = -1;
		turnValue = drive.getAutonTurn();
		gyro.resetGyro();
		turnAngle = 0.0;
	}

	boolean reset = false;

	protected void execute() {
		gyroAngle = gyro.getGyroAngle();
		if (!intake.isAtTop()) {
			intake.moveLiftUp();
		} else {
			intake.liftStop();
		}
		if (position != 0) {
			String direction = networkTable.getString("ShootDirection", "robotUnknown");
			if (!reset) {
				double shooterRads = networkTable.getNumber("ShootRads", 0.0);
				gyro.resetGyro();
				reset = true;
			}
			/*
			 * if (turnTime - System.currentTimeMillis() <= -100) { double
			 * shooterRads = networkTable.getNumber("ShootRads", 0.0); turnTime
			 * = System.currentTimeMillis() + angleToTime(shooterRads);
			 * SmartDashboard.putNumber("angleToTime",
			 * angleToTime(shooterRads)); reset = false; }
			 */
			// double boxDistance = networkTable.getNumber("ShootDistance", 0);
			// SmartDashboard.putNumber("TimeLeftofTurn", turnTime -
			// System.currentTimeMillis());
			if (direction.equals("left")) {
				shoot.shoot(0);

				if (gyroAngle - turnAngle > 0) {
					drive.arcadeDrive(0.0, (-turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
				} else {
					drive.arcadeDrive(0, 0);
					networkTable.putNumber("ShootRads", 0.0);
					reset = false;
					// turnTime = -1;
				}
				startTime = -1;
			} else if (direction.equals("right")) {
				if (gyroAngle - turnAngle > 0) {
					drive.arcadeDrive(0.0, (turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
				} else {
					drive.arcadeDrive(0, 0);
					networkTable.putNumber("ShootRads", 0.0);
					reset = false;
					// turnTime = -1;
				}
				shoot.shoot(0);
				startTime = -1;
			} else if (direction.equals("forward")) {
				shoot.shoot(0);
				drive.arcadeDrive(0.25, 0.0);
				startTime = -1;
			} else if (direction.equals("backward")) {
				shoot.shoot(0);
				drive.arcadeDrive(-0.25, 0.0);
				startTime = -1;
			} else if (direction.equals("shoot")) {
				if (!scooper.isAtLowerLimit()) { // Lowers scooper if not at
													// lower limit
					scooper.moveScooperDown();
				} else {
					scooper.scooperStop();
				}
				drive.arcadeDrive(0, 0);
				if (startTime == -1) {
					startTime = System.currentTimeMillis();
				}
				if (startTime != -1 && System.currentTimeMillis() - startTime > 500) {
					shoot.shoot(speed);
				}
				if (startTime != -1 && System.currentTimeMillis() - startTime > 750 && shoot.getLeftSpeed() >= speed
						&& shoot.getRightSpeed() >= speed) {// Speed
					intake.intakeBall();
				}
			} else if (direction.equals("unknown")) {
				// Add Lift If 1
				if (position < 3) {
					drive.arcadeDrive(0, 0.25);
				} else {
					drive.arcadeDrive(0, -0.25);
				}
			}
		}
	}

	// returns true if auto mode is done, if not it returns false; uses
	// startTime, position, and the current system time
	protected boolean isFinished() {
		return (startTime != -1 && System.currentTimeMillis() - startTime > 4000) || position == 0;
	}

	// ends shoot and arcadeDrive
	protected void end() {
		drive.arcadeDrive(0, 0);
		shoot.shoot(0);
	}

	protected void interrupted() {
		end();
	}

	public double angleToTime(double shooterRads) {
		if (shooterRads == 0.0) {
			return -100;
		}
		return (SmartDashboard.getNumber("RadsThreshold", 1.0) - shooterRads)
				* SmartDashboard.getNumber("StallTime", 600);
	}

}
