package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.PrecisionCyborgController;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
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
	DriverStation driverStation;

	// sets up AutoShoot
	public AutoShoot() {
		drive = Robot.getDriveTrain();
		shoot = Robot.getShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		networkTable = NetworkTable.getTable("imageProcessing");
		SmartDashboard.putNumber("StallTime", 1400);
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
	}

	protected void execute() {
		if (position != 0) {
			String direction = networkTable.getString("ShootDirection", "robotUnknown");
			if (turnTime < 0) {
				double shooterRads = networkTable.getNumber("ShootRads", 0.0);
				turnTime = System.currentTimeMillis() + angleToTime(shooterRads);
				SmartDashboard.putNumber("angleToTime", angleToTime(shooterRads));
			}
			// double boxDistance = networkTable.getNumber("ShootDistance", 0);
			SmartDashboard.putNumber("TimeLeftofTurn", turnTime - System.currentTimeMillis());
			if (direction.equals("left")) {
				shoot.shoot(0);
				if (turnTime - System.currentTimeMillis() >= 0) {
					drive.arcadeDrive(0.0, (-turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
				} else {
					drive.arcadeDrive(0, 0);
					networkTable.putNumber("ShootRads", 0.0);
					turnTime = -1;
				}
				startTime = -1;
			} else if (direction.equals("right")) {
				if (turnTime - System.currentTimeMillis() >= 0) {
					drive.arcadeDrive(0.0, (turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
				} else {
					drive.arcadeDrive(0, 0);
					networkTable.putNumber("ShootRads", 0.0);
					turnTime = -1;
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
					shoot.shoot(0.6);
				}
				if (startTime != -1 && System.currentTimeMillis() - startTime > 750 && shoot.getLeftSpeed() >= 0.6) {// Speed
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
			return 0;
		}
		return (1.0 - shooterRads) * SmartDashboard.getNumber("StallTime", 1400);
	}

}
