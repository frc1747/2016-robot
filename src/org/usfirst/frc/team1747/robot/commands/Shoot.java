package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.LeftShooter;
import org.usfirst.frc.team1747.robot.subsystems.RightShooter;

public class Shoot extends Command {

	private LeftShooter leftShooter;
	private RightShooter rightShooter;
	private Intake intake;
	private double timeToSettle;

	public Shoot() {
		leftShooter = Robot.getLeftShooter();
		rightShooter = Robot.getRightShooter();
		intake = Robot.getIntake();
		requires(leftShooter);
		requires(rightShooter);
		requires(intake);
	}

	protected void initialize() {
		timeToSettle = 0;
		leftShooter.setSetpoint(leftShooter.getTargetShooterSpeed());
		leftShooter.pidEnable();
		rightShooter.setSetpoint(rightShooter.getTargetShooterSpeed());
		rightShooter.pidEnable();
	}

	protected void execute() {
		if(leftShooter.isAtTarget() && rightShooter.isAtTarget()) {
			if(timeToSettle == 0) {
				timeToSettle = timeSinceInitialized();
				SmartDashboard.putNumber("Time to Settle", timeToSettle);
			}
			intake.intakeBall();
		}
	}

	protected boolean isFinished() {
		return false;
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