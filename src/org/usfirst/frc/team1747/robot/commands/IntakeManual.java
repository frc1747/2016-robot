package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.OI;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

public class IntakeManual extends Command {

	private Intake intake;
	private OI oi;

	public IntakeManual() {
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
		if (oi.getAuxController().getDPad() != -1) {
			if (oi.getAuxController().getDPad() == 90) {
				intake.liftStop();
				intake.rollerControl(-.75);
			} else if (oi.getAuxController().getDPad() == 270) {
				intake.liftStop();
				intake.rollerControl(.75);
			} else if (oi.getAuxController().getDPad() == 0) {
				intake.rollerStop();
				intake.liftControl(0.8);
			} else if (oi.getAuxController().getDPad() == 180) {
				intake.rollerStop();
				intake.liftControl(-0.5);
			}
		} else {
			intake.rollerStop();
			intake.liftStop();
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
