package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

public class IntakeBall extends Command {

	private Intake intake;

	// liftInput senses if the arm is low enough to get the ball
	// intakeInput senses if we have a ball
	public IntakeBall() {
		intake = Robot.getIntake();
		requires(intake);
	}

	@Override
	protected void initialize() {
		if(!intake.hasBall()){
			intake.intakeBall();
		}
		
	}

	// Pick up a ball
	@Override
	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		return intake.hasBall();
	}

	// The intake stops when a ball is sensed in the robot
	@Override
	protected void end() {
		intake.rollerControl(0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}