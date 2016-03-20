package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LowerLift extends Command {

	Intake intake;

	public LowerLift() {
		intake = Robot.getIntake();
		requires(intake);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		intake.moveLiftDown();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return intake.isAtBottom();
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
