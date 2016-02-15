package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class AutoShoot extends Command {

	DriveTrain drive;
	Shooter shoot;
	Intake intake;
	NetworkTable networkTable;
	double startTime;

	public AutoShoot() {
		drive = Robot.getDrive();
		shoot = Robot.getShooter();
		intake = Robot.getIntake();
		networkTable = NetworkTable.getTable("imageProcessing");
		requires(shoot);
		requires(drive);
		requires(intake);
	}

	protected void initialize() {
		startTime = -1;
		System.out.println("Running");
	}

	protected void execute() {
		String direction = networkTable.getString("ShootDirection", "unknown");
		if (direction.equals("left")) {
			shoot.shoot(0);
			drive.arcadeDrive(0.0, -0.225);
			startTime = -1;
		} else if (direction.equals("right")) {
			shoot.shoot(0);
			drive.arcadeDrive(0.0, 0.225);
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
			drive.arcadeDrive(0, 0);
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			}
			if (startTime != -1 && System.currentTimeMillis() - startTime > 500) {
				shoot.shoot(0.6);
			}
			if (startTime != -1 && System.currentTimeMillis() - startTime > 3000) {
				intake.intakeBall();
			}
		} else if (direction.equals("unknown")) {
			drive.arcadeDrive(0, -0.3);
		}
	}

	protected boolean isFinished() {
		return startTime != -1 && System.currentTimeMillis() - startTime > 4000;
	}

	protected void end() {
		drive.arcadeDrive(0, 0);
		shoot.shoot(0);
	}

	protected void interrupted() {
		end();
	}

}
