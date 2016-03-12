package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.PrecisionCyborgController;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShoot extends Command {

	DriveTrain drive;
	Shooter shoot;
	Intake intake;
	PrecisionCyborgController auxController;
	NetworkTable networkTable;
	double speed;
	double startTime;
	int position;
	double turnValue;

	// sets up AutoShoot
	public AutoShoot() {
		drive = Robot.getDriveTrain();
		shoot = Robot.getShooter();
		intake = Robot.getIntake();
		speed = SmartDashboard.getNumber("Target Shooter Speed", .6);
		networkTable = NetworkTable.getTable("imageProcessing");
		requires(shoot);
		requires(drive);
		requires(intake);
	}

	// initializes AutoShoot then prints out that it is running
	protected void initialize() {
		position = Robot.getSd().getAutonPosition();
		startTime = -1;
		turnValue = drive.getAutonTurn();
	}

	protected void execute() {
		if (position != 0) {
			String direction = networkTable.getString("ShootDirection", "robotUnknown");
			// double boxDistance = networkTable.getNumber("ShootDistance", 0);
			switch (direction) {
			case "left":
				shoot.shoot(0);
				drive.arcadeDrive(0.0, (-turnValue));
				startTime = -1;
				break;
			case "right":
				shoot.shoot(0);
				drive.arcadeDrive(0.0, turnValue);
				startTime = -1;
				break;
			case "forward":
				shoot.shoot(0);
				drive.arcadeDrive(0.25, 0.0);
				startTime = -1;
				break;
			case "backward":
				shoot.shoot(0);
				drive.arcadeDrive(-0.25, 0.0);
				startTime = -1;
				break;
			case "shoot":
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
				break;
			case "unknown":
				// Add Lift If 1
				if (position < 3) {
					System.out.println("Lesser");
					drive.arcadeDrive(0, 0.25);
				} else {
					System.out.println("Greater");
					drive.arcadeDrive(0, -0.25);
				}
				break;
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
