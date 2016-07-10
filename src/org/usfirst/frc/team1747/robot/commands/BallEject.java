package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class BallEject extends Command {

	private Intake intake;

	// uses intake
	public BallEject() {
		intake = Robot.getIntake();
		requires(intake);
	}

	@Override
	protected void initialize() {
		intake.ejectBall();
	}

	// Eject the ball
	@Override
	protected void execute() {
	}

	// After 2000milliseconds, stop the motors
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		intake.rollerStop();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
