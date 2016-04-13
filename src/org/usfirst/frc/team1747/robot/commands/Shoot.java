package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shoot extends Command {

	private Shooter shooter;
	private Intake intake;
	private boolean pidMode;
	private long startTime = -1;
	private double speed;

	public Shoot() {
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		requires(shooter);
		requires(intake);
	}

	@Override
	protected void initialize() {
		pidMode = SmartDashboard.getBoolean("Shooter PID Mode", true);
		speed = SmartDashboard.getNumber("Target Shooter Speed", .65);
		startTime = -1;
		if (pidMode) {
			shooter.enablePID();
			shooter.setSetpoint(speed);
		} else {
			shooter.shoot(speed);
		}
	}

	@Override
	protected void execute() {
		if (pidMode) {
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
		} else if (shooter.getLeftSpeed() > speed && shooter.getRightSpeed() > speed) {
			// intake.intakeBall();
			// startTime = System.currentTimeMillis();
		}

	}

	@Override
	protected boolean isFinished() {
		return startTime != -1 && System.currentTimeMillis() - startTime > 2000;
	}

	@Override
	protected void end() {
		if (pidMode) {
			shooter.disablePID();
		}
		shooter.shoot(0.0);
		intake.rollerStop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}