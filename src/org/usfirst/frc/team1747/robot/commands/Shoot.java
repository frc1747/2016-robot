package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class Shoot extends Command {

	Shooter shooter;
	double speed;
	double startTime;

	public Shoot(double speed) {
		shooter = Robot.getShooter();
		requires(shooter);
		this.speed = speed;
	}

	protected void initialize() {
		System.out.println(speed);
		shooter.shoot(speed);
	}

	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		shooter.shoot(0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
