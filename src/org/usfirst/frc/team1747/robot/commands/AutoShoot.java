package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.PrecisionCyborgController;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShoot extends Command {

	PrecisionCyborgController auxController;
	private DriveTrain drive;
	private Shooter shoot;
	private Intake intake;
	private Scooper scooper;
	private NetworkTable networkTable;
	private double speed;
	private double startTime;
	private SDController.Positions position;
	private double turnValue;
	private double turnTime;
	private DriverStation driverStation;
	private boolean reset = false;
	private double gyroAngle;
	private double turnAngle;

	// sets up AutoShoot
	public AutoShoot() {
		drive = Robot.getDriveTrain();
		shoot = Robot.getShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		networkTable = NetworkTable.getTable("imageProcessing");
		SmartDashboard.putNumber("StallTime", 800);
		SmartDashboard.putNumber("RadsThreshhold", .95);
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
		drive.resetGyro();
		turnAngle = 0.0;
	}

	protected void execute() {
		gyroAngle = drive.getTurnAngle();
		if (!intake.isAtTop()) {
			intake.moveLiftUp();
		} else {
			intake.liftStop();
		}
		if (position != SDController.Positions.NOTHING) {
			String direction = networkTable.getString("ShootDirection", "robotUnknown");

			if (reset) {
				turnAngle = networkTable.getNumber("GyroAngle", 0.0);
				double shooterRads = networkTable.getNumber("ShootRads", 0.0);
				drive.resetGyro();
				reset = false;
			}
			if (SmartDashboard.getBoolean("LastSecondShot", false) && driverStation.isAutonomous()
					&& !direction.equals("robotUnknown") && driverStation.getMatchTime() < 3) {
				direction = "shoot";
			}
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
					reset = true;
				}
				startTime = -1;
			} else if (direction.equals("right")) {
				if (gyroAngle - turnAngle > 0) {
					drive.arcadeDrive(0.0, (turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
				} else {
					drive.arcadeDrive(0, 0);
					networkTable.putNumber("ShootRads", 0.0);
					reset = true;
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
				if (position == SDController.Positions.ONE || position == SDController.Positions.TWO
						|| position == SDController.Positions.THREE) {
					drive.arcadeDrive(0, 1.5 * turnValue);
				} else {
					drive.arcadeDrive(0, 1.5 * -turnValue);
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
		shoot.shoot(0);
	}

	protected void interrupted() {
		end();
	}

	private double angleToTime(double shooterRads) {
		if (shooterRads == 0.0) {
			return -100;
		}
		return (SmartDashboard.getNumber("RadsThreshold", 0.95) - shooterRads)
				* SmartDashboard.getNumber("StallTime", 800);
	}
}
