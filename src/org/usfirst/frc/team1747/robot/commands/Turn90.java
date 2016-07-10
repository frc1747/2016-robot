package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrainPID;

import edu.wpi.first.wpilibj.command.Command;

public class Turn90 extends Command {

	private DriveTrain driveTrain;
	private DriveTrainPID pid;

	public Turn90() {
		driveTrain = Robot.getDriveTrain();
		pid = Robot.getDriveTrainPID();
		requires(pid);
		requires(driveTrain);
	}

	protected void initialize() {
		driveTrain.resetGyro();
		pid.setSetPoint(90.0);
		pid.pidEnable();

		// driveTrain.resetGyro();
		// driveTrain.enablePID();
		// driveTrain.setSetpoint(90);
	}

	@Override
	protected void execute() {
		// driveTrain.runPID();
	}

	@Override
	protected boolean isFinished() {
		return pid.isAtTarget();
		// return driveTrain.isAtTarget();
	}

	@Override
	protected void end() {
		pid.pidDisable();
		// driveTrain.disablePID();
		// driveTrain.arcadeDrive(0.0, 0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}
