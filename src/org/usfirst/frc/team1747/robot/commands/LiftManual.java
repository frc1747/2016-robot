package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.OI;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftManual extends Command {

	Intake intake;
	OI oi;

	public LiftManual() {
		intake = Robot.getIntake();
		oi = Robot.getOi();
		requires(intake);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (oi.getController().getDPad() != -1) {
			if (oi.getController().getDPad() == 0) {
				intake.moveLiftUp();
				;
			} else if (oi.getController().getDPad() == 180) {
				intake.moveLiftDown();
			} else {
				intake.liftControl(0);
			}
		} else {
			intake.liftControl(0);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
