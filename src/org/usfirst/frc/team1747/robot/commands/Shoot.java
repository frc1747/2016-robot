package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

public class Shoot extends Command {

	private Shooter shooter;
	private Intake intake;
	private Scooper scooper;
	private double timeToSettle;

	public Shoot() {
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		requires(shooter);
		requires(intake);
		requires(scooper);
	}

	protected void initialize() {
		timeToSettle = 0;
		shooter.setSetpoint(shooter.getTargetShooterSpeed());
		shooter.pidEnable();
		if (!scooper.isAtLowerLimit()) {
			scooper.moveScooperDown();
		} else {
			scooper.scooperStop();
		}
	}

	protected void execute() {
		if(shooter.isAtTarget()) {
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
		shooter.pidDisable();
		shooter.setSpeed(0.0);
		intake.rollerStop();
	}

	protected void interrupted() {
		end();
	}
}