package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

public class BallEject extends Command {

	private Intake intake;
	private double startTime;
	//uses intake 
	public BallEject() {
		intake = Robot.getIntake();
		requires(intake);
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
	}

	// Eject the ball
	@Override
	protected void execute() {
		intake.ejectBall();
	}

	// After 2000milliseconds, stop the motors
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		intake.rollerControl(0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}
