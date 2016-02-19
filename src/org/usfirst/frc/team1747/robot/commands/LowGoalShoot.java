package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class LowGoalShoot extends Command {

	Shooter shooter;
	Intake intake;
	double startTime;
	double time = -1;
	boolean pidMode;

	public LowGoalShoot() {
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		requires(shooter);
		requires(intake);
	}

	@Override
	protected void initialize() {
		shooter.shoot(.2);
		time = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		if (shooter.getLeftSpeed() >= 0.14 && shooter.getRightSpeed() >= 0.14) {
			intake.intakeBall();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		shooter.shoot(0.0);
		intake.rollerControl(0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
