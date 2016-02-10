package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class AutoShoot extends Command {

	DriveTrain drive;
	Shooter shoot;
	NetworkTable networkTable;

	public AutoShoot() {
		drive = Robot.getDrive();
		shoot = Robot.getShooter();
		networkTable = NetworkTable.getTable("imageProcessing");
		requires(shoot);
		requires(drive);
	}

	protected void initialize() {
		System.out.println("Running");
	}

	protected void execute() {
		String direction = networkTable.getString("ShootDirection", "unknown");
		System.out.println(direction);
		if (direction.equals("left")) {
			drive.arcadeDrive(0.0, -0.225);
		}
		if (direction.equals("right")) {
			drive.arcadeDrive(0.0, 0.225);
		}
		if (direction.equals("forward")) {
			drive.arcadeDrive(0.25, 0.0);
		}
		if (direction.equals("backward")) {
			drive.arcadeDrive(-0.25, 0.0);
		}
		if (direction.equals("shoot")) {
			drive.arcadeDrive(0, 0);
		}
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		drive.arcadeDrive(0, 0);
	}

	protected void interrupted() {
		end();
	}

}
