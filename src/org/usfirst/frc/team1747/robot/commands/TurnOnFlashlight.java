package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Flashlight;

import edu.wpi.first.wpilibj.command.Command;

public class TurnOnFlashlight extends Command {

	Flashlight flashlight;

	public TurnOnFlashlight() {
		flashlight = Robot.getFlashlight();
		requires(flashlight);
	}

	protected void initialize() {
		flashlight.turnOnFlashlight();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		flashlight.turnOffFlashlight();
	}

	protected void interrupted() {
		end();
	}

}
