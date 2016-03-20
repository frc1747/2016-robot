package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.PrecisionCyborgController;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleAutoShoot extends Command {

	DriveTrain drive;
	Shooter shoot;
	Intake intake;
	PrecisionCyborgController auxController;
	NetworkTable networkTable;
	double speed;
	double startTime;
	int position;
	double turnValue;
	DriverStation driverStation;
	boolean seenGoal;

	// sets up AutoShoot
	public TeleAutoShoot() {
		drive = Robot.getDriveTrain();
		shoot = Robot.getShooter();
		intake = Robot.getIntake();
		networkTable = NetworkTable.getTable("imageProcessing");
		driverStation = DriverStation.getInstance();
		seenGoal = false;
		requires(shoot);
		requires(drive);
		requires(intake);
	}

	// initializes AutoShoot then prints out that it is running
	protected void initialize() {
		speed = SmartDashboard.getNumber("Target Shooter Speed", .6);
		position = Robot.getSd().getAutonPosition();
		startTime = -1;
		turnValue = drive.getAutonTurn();
	}

	protected void execute() {
		if (position != 0) {
			String direction = networkTable.getString("ShootDirection", "robotUnknown");
			// double boxDistance = networkTable.getNumber("ShootDistance", 0);
			if (!seenGoal) {
				if (direction.equals("left")) {
					shoot.shoot(0);
					drive.arcadeDrive(0.0, (-turnValue) * (driverStation.isAutonomous() ? 1 : 1.5));
					startTime = -1;
				} else if (direction.equals("right")) {
					shoot.shoot(0);
					drive.arcadeDrive(0.0, turnValue * (driverStation.isAutonomous() ? 1 : 1.5));
					startTime = -1;
				} else if (direction.equals("forward")) {
					shoot.shoot(0);
					drive.arcadeDrive(0.25, 0.0);
					startTime = -1;
				} else if (direction.equals("backward")) {
					shoot.shoot(0);
					drive.arcadeDrive(-0.25, 0.0);
					startTime = -1;
				}
				if (direction.equals("shoot")) {
					seenGoal = true;
					drive.arcadeDrive(0, 0);
					if (startTime == -1) {
						startTime = System.currentTimeMillis();
					}
					if (startTime != -1 && System.currentTimeMillis() - startTime > 500) {
						shoot.shoot(0.6);
					}
					if (startTime != -1 && shoot.getRightSpeed() >= speed) {
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

}