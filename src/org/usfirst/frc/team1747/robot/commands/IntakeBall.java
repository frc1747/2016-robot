package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.OI;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeBall extends Command {

	Intake intake;

	// liftInput senses if the arm is low enough to get the ball
	// intakeInput senses if we have a ball
	public IntakeBall() {
		intake = Robot.getIntake();
		requires(intake);
	}

	@Override
	protected void initialize() {
		//Robot.getOi().setCancelIntakeCommand();
	}

	// Pick up a ball
	@Override
	protected void execute() {
		intake.intakeBall();
	}

	@Override
	protected boolean isFinished() {
		return intake.hasBall();
	}

	// The intake stops when a ball is sensed in the robot
	@Override
	protected void end() {
		intake.rollerControl(0);
		Robot.getOi().setIntakeCommand();
	}

	@Override
	protected void interrupted() {
		intake.rollerControl(0);
	}

}