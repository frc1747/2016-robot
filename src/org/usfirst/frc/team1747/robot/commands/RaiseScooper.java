package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;

import edu.wpi.first.wpilibj.command.Command;

public class RaiseScooper extends Command {

	Scooper scooper;
	double startTime;

	public RaiseScooper() {
		scooper = Robot.getScooper();
		requires(scooper);
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();

	}

	@Override
	protected void execute() {
		scooper.moveScooperUp();
	}

	@Override
	protected boolean isFinished() {
		return (scooper.isAtUpperLimit() || (System.currentTimeMillis() - startTime >= 300));
	}

	@Override
	protected void end() {
		scooper.scooperStop();
	}

	@Override
	protected void interrupted() {
		end();
	}

}