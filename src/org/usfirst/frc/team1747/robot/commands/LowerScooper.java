package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;

import edu.wpi.first.wpilibj.command.Command;

public class LowerScooper extends Command {

	Scooper scooper;
	double startTime;

	public LowerScooper() {
		scooper = Robot.getScooper();
		requires(scooper);
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();

	}

	@Override
	protected void execute() {
		scooper.moveScooperDown();
	}

	@Override
	protected boolean isFinished() {
		return (scooper.isAtLowerLimit() || (System.currentTimeMillis() - startTime >= 300));
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