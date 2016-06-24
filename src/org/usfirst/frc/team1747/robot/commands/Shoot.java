package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.LeftShooter;

public class Shoot extends Command {

	private LeftShooter shooter;
	private Intake intake;
	private long startTime = -1;

	public Shoot() {
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		requires(shooter);
		requires(intake);
	}

	protected void initialize() {
		startTime = -1;
		shooter.setSetpoint(shooter.getTargetShooterSpeed());
		shooter.enablePID();
	}

	protected void execute() {
		shooter.runPID();
		if (shooter.isAtTarget()) {
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
		shooter.disablePID();
		shooter.shoot(0.0);
		intake.rollerStop();
	}

	protected void interrupted() {
		end();
	}
}