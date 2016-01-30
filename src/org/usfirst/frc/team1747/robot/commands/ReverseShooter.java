package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class ReverseShooter extends Command {
	Shooter shooter;

	public ReverseShooter() {
		shooter = Robot.getShooter();
		requires(shooter);
	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		shooter.shoot(-.1);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		shooter.shoot(0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}
