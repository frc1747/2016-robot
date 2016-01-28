package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeBall extends Command {

	Intake intake;
	DigitalInput liftInput, intakeInput;

	// input senses if the arm is low enough to get the ball
	// input2 senses if we have a ball

	public IntakeBall() {
		intake = Robot.getIntake();
		liftInput = intake.getLiftInput();
		intakeInput = intake.getIntakeInput();
		requires(intake);
	}

	protected void initialize() {
		intake.liftControl(0.5);
		if (liftInput.get()) {
			intake.liftControl(0);
		}
	}

	protected void execute() {
		intake.rollerControl(0.5);
	}

	@Override
	protected boolean isFinished() {
		return intakeInput.get();
	}

	protected void end() {
		intake.rollerControl(0);
	}

	protected void interrupted() {
	}

}