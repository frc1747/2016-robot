package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightBack extends Command {
	DriveTrain driveTrain;
	double time;
	int position;

	public DriveStraightBack() {
		driveTrain = Robot.getDriveTrain();
		requires(driveTrain);
	}

	// sets up tankDrive
	@Override
	protected void initialize() {
		position = Robot.getSd().getAutonPosition();
		time = System.currentTimeMillis();
		driveTrain.tankDrive(-.5, .5);
	}

	@Override
	protected void execute() {
	}

	// returns true if more than 1500 time has passed
	@Override
	protected boolean isFinished() {
		return System.currentTimeMillis() - time > 1500;
	}

	@Override
	protected void end() {
		driveTrain.tankDrive(0.0, 0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
