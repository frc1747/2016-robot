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
		// System.out.println("Constructor");
		// SmartDashboard.putNumber("DriveStraight Distance", 100.0);
		requires(driveTrain);
	}

	// sets up tankDrive
	@Override
	protected void initialize() {
		// System.out.println("Initialize");
		// double distance = SmartDashboard.getNumber("DriveStraight Distance",
		// 1000000.0);
		// driveTrain.enablePID();
		// driveTrain.setSetpoint(distance);\
		position = Robot.getSd().getAutonPosition();
		time = System.currentTimeMillis();
		driveTrain.tankDrive(-.25, .5);
	}

	@Override
	protected void execute() {
		// driveTrain.runPID();
	}

	// returns true if more than 3250 time has passed
	@Override
	protected boolean isFinished() {
		if (position == 1 || position == 5) {
			return System.currentTimeMillis() - time > 3750;
		} else {
			return System.currentTimeMillis() - time > 3250;
		}
		// return driveTrain.isAtTarget();
	}

	@Override
	protected void end() {
		// System.out.println("End");
		// driveTrain.disablePID();
		driveTrain.tankDrive(0.0, 0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
