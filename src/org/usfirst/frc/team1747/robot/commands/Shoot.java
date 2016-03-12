package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shoot extends Command {

	Shooter shooter;
	Intake intake;
	double speed;
	double startTime;
	double time = -1;
	boolean pidMode;

	public Shoot() {
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		requires(shooter);
		requires(intake);
	}

	@Override
	protected void initialize() {
		pidMode = SmartDashboard.getBoolean("Shooter PID Mode", true);
		double speed = SmartDashboard.getNumber("Target Shooter Speed", .6);
		this.speed = speed;
		if (pidMode) {
			speed *= -20.0;
			shooter.enablePID();
			shooter.setSetpoint(speed);
		} else {
			shooter.shoot(speed);
		}
		time = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		System.out.println(shooter.getLeftSpeed());
		System.out.println(shooter.getRightSpeed());
		if (pidMode) {
			shooter.runPID();
		}
		if (shooter.getRightSpeed() >= speed) {	
			intake.intakeBall();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		if (pidMode) {
			shooter.disablePID();
		}
		shooter.shoot(0.0);
		intake.rollerControl(0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}