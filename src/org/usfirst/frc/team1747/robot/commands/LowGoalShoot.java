package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

public class LowGoalShoot extends Command {

	double startTime;
	boolean pidMode;
	private Shooter shooter;
	private Intake intake;

	public LowGoalShoot() {
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		requires(shooter);
		requires(intake);
	}

	@Override
	protected void initialize() {
		shooter.setSpeed(0.2);
	}

	@Override
	protected void execute() {
		if (shooter.getLeftSpeed() >= 14 && shooter.getRightSpeed() >= 14) {
			intake.intakeBall();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		shooter.setSpeed(0.0);
		intake.rollerControl(0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
