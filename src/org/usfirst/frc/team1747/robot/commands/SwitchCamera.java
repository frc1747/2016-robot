package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.OI;
import org.usfirst.frc.team1747.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SwitchCamera extends Command {

	boolean shooterCamera = true;
	OI oi;

	public SwitchCamera() {
		oi = Robot.getOi();
	}

	protected void initialize() {
	}
	
	protected void execute() {
		if (shooterCamera) {
			oi.getShooterCamera().startCapture();
			oi.getIntakeCamera().stopCapture();

		} else {
			oi.getShooterCamera().stopCapture();
			oi.getIntakeCamera().startCapture();
		}
		shooterCamera = !shooterCamera;
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
