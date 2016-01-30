package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class BallEject extends Command {

	Intake intake;
	double startTime;

	public BallEject() {
		intake = Robot.getIntake();
		requires(intake);
	}

	protected void initialize() {
		startTime = System.currentTimeMillis();
	}

	// Eject the ball
	protected void execute() {
		intake.ejectBall();
	}

	// After 2000milliseconds, stop the motors
	protected boolean isFinished() {
		return (System.currentTimeMillis() - startTime >= 2000);
	}

	protected void end() {
		intake.rollerControl(0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}
