package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

/**
 *
 */
public class LowerLift extends Command {

	private Intake intake;
	private double startTime;

	public LowerLift() {
		intake = Robot.getIntake();
		requires(intake);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		intake.moveLiftDown();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return intake.isAtBottom() || (System.currentTimeMillis() - startTime >= 1500);
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		intake.liftControl(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
