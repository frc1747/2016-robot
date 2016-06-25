package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.LeftShooter;
import org.usfirst.frc.team1747.robot.subsystems.RightShooter;

public class LowGoalShoot extends Command {

	double startTime;
	boolean pidMode;
	private LeftShooter leftShooter;
	private RightShooter rightShooter;
	private Intake intake;
	private double time = -1;

	public LowGoalShoot() {
		leftShooter = Robot.getLeftShooter();
		rightShooter = Robot.getRightShooter();
		intake = Robot.getIntake();
		requires(leftShooter);
		requires(rightShooter);
		requires(intake);
	}

	@Override
	protected void initialize() {
		leftShooter.setSpeed(0.2);
		rightShooter.setSpeed(0.2);
		time = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		if (leftShooter.getSpeed() >= 14 && rightShooter.getSpeed() >= 14) {
			intake.intakeBall();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		leftShooter.setSpeed(0.0);
		rightShooter.setSpeed(0.0);
		intake.rollerControl(0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
