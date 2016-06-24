package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.LeftShooter;

public class LowGoalShoot extends Command {

	double startTime;
	boolean pidMode;
	private LeftShooter shooter;
	private Intake intake;
	private double time = -1;

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
