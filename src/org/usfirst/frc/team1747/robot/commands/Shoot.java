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
		System.out.println("shooting!!!!");
		this.speed = speed;
		// startTime = System.currentTimeMillis();
	}

	protected void initialize() {
		System.out.println(speed);
		shooter.shoot(speed);
	}

	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		// System.out.println(System.currentTimeMillis() - startTime);
		return false;// System.currentTimeMillis() - startTime > 50000.0;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		// shooter.shoot(0.0);
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
