package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.LeftShooter;
import org.usfirst.frc.team1747.robot.subsystems.RightShooter;

public class Shoot extends Command {

	private LeftShooter leftShooter;
	private RightShooter rightShooter;
	private Intake intake;
	private long startTime = -1;

	public Shoot() {
		leftShooter = Robot.getLeftShooter();
		rightShooter = Robot.getRightShooter();
		intake = Robot.getIntake();
		requires(leftShooter);
		requires(rightShooter);
		requires(intake);
	}

	protected void initialize() {
		startTime = -1;
		leftShooter.setSetpoint(leftShooter.getTargetShooterSpeed());
		leftShooter.pidEnable();
		rightShooter.setSetpoint(rightShooter.getTargetShooterSpeed());
		rightShooter.pidEnable();
	}

	protected void execute() {
		if (leftShooter.isAtTarget() && rightShooter.isAtTarget()) {
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else if (System.currentTimeMillis() - startTime > 500) {
				intake.intakeBall();
			}
		} else {
			startTime = -1;
		}
	}

	protected boolean isFinished() {
		return startTime != -1 && System.currentTimeMillis() - startTime > 2000;
	}

	protected void end() {
		leftShooter.pidDisable();
		leftShooter.setSpeed(0.0);
		rightShooter.pidDisable();
		rightShooter.setSpeed(0.0);
		intake.rollerStop();
	}

	protected void interrupted() {
		end();
	}
}