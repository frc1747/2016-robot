package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.OI;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;

import edu.wpi.first.wpilibj.command.Command;

public class ScooperManual extends Command {

	Scooper scooper;
	OI oi;

	public ScooperManual() {
		scooper = Robot.getScooper();
		oi = Robot.getOi();
		requires(scooper);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (oi.getAuxController().getRightBumper().get()) {
			scooper.moveScooperUp();
		} else if (oi.getAuxController().getLeftBumper().get()) {
			scooper.moveScooperDown();
		} else {
			scooper.scooperStop();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		end();
	}

}
