package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeBall extends Command {

	Intake intake;

	// liftInput senses if the arm is low enough to get the ball
	// intakeInput senses if we have a ball
	public IntakeBall() {
		intake = Robot.getIntake();
		requires(intake);
	}

	protected void initialize() {

	}

	// Pick up a ball
	protected void execute() {
		intake.intakeBall();
		if (!intake.isAtBottom()) {
			intake.moveLiftDown();
		} else {
			intake.liftControl(0);
		}
	}

	protected boolean isFinished() {
		return intake.hasBall();
	}

	// The intake stops when a ball is sensed in the robot
	protected void end() {
		intake.rollerControl(0);
		intake.liftControl(0);
	}

	protected void interrupted() {
		end();
	}

}