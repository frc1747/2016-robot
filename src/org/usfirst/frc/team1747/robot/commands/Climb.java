package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {

	Climber climber;

	public Climb() {
		climber = Robot.getClimber();
		requires(climber);
	}

	@Override
	protected void initialize() {
		climber.climbUp();
	}

	@Override
	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		climber.stopClimb();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
