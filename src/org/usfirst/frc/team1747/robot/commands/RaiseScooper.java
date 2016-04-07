package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;

public class RaiseScooper extends Command {

	private Scooper scooper;
	private double startTime;

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
		return (scooper.isAtUpperLimit() || (System.currentTimeMillis() - startTime >= 1000));
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