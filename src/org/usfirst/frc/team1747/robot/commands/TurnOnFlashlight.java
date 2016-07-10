package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class TurnOnFlashlight extends Command {

	Shooter shooter;

	public TurnOnFlashlight() {
		shooter = Robot.getShooter();
		requires(shooter);
	}

	protected void initialize() {
		shooter.turnOnFlashlight();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		shooter.turnOffFlashlight();
	}

	protected void interrupted() {
		end();
	}

}
