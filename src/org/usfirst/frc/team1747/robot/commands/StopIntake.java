package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.OI;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class StopIntake extends Command {
	Intake intake;
	OI oi;

	public StopIntake() {
		oi = Robot.getOi();
		intake = Robot.getIntake();
		requires(intake);
	}

	@Override
	protected void initialize() {
		intake.rollerStop();
		oi.setIntakeCommand();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
