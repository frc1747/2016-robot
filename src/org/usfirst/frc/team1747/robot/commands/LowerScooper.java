package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;

public class LowerScooper extends Command {

	private Scooper scooper;
	private double startTime;

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
		return (scooper.isAtLowerLimit() || (System.currentTimeMillis() - startTime >= 1000));
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