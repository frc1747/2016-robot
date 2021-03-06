package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrainPID;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turn90 extends Command {

	private DriveTrain driveTrain;
	private DriveTrainPID drivePID;

	public Turn90() {
		driveTrain = Robot.getDriveTrain();
		drivePID = Robot.getDriveTrainPID();
		requires(drivePID);
		requires(driveTrain);
		
		SmartDashboard.putNumber("Turn90 Angle", 90.0);
	}

	protected void initialize() {
		driveTrain.resetGyro();
		drivePID.setSetpoint(SmartDashboard.getNumber("Turn90 Angle", 90.0));
		drivePID.pidEnable();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return drivePID.isAtTarget();
	}

	@Override
	protected void end() {
		drivePID.pidDisable();
		driveTrain.arcadeDrive(0.0, 0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}
