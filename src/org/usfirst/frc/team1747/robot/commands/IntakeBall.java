package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeBall extends Command {

	Intake intake;
	DigitalInput liftInput, intakeInput;

	// liftInput senses if the arm is low enough to get the ball
	// intakeInput senses if we have a ball

	public IntakeBall() {
		intake = Robot.getIntake();
		liftInput = intake.getLiftInput();
		intakeInput = intake.getIntakeInput();
		requires(intake);
	}

	// Move the arm into position to pick up a ball
	protected void initialize() {
		intake.liftControl(0.5);
		if (liftInput.get()) {
			intake.liftControl(0);
		}
	}

	// Pick up a ball
	protected void execute() {
		intake.rollerControl(0.5);
	}

	protected boolean isFinished() {
		return intakeInput.get();
	}

	// The intake stops when a ball is sensed in the robot
	protected void end() {
		intake.rollerControl(0);
	}

	protected void interrupted() {
	}

}